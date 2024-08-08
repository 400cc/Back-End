package Designovel.Capstone.api.styleFilter.queryDSL;

import Designovel.Capstone.api.styleFilter.dto.StyleFilterDTO;
import Designovel.Capstone.domain.style.style.StyleId;
import Designovel.Capstone.domain.style.styleRanking.QStyleRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.categoryStyle;
import static Designovel.Capstone.domain.image.QImage.image;
import static Designovel.Capstone.domain.style.style.QStyle.style;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StyleFilterQueryDSLImpl implements StyleFilterQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(StyleFilterDTO filter) {
        QStyleRanking subStyleRanking = new QStyleRanking("subStyleRanking");
        JPQLQuery<LocalDate> subQuery = JPAExpressions.select(subStyleRanking.crawledDate.max())
                .from(subStyleRanking)
                .where(subStyleRanking.styleId.eq(styleRanking.styleId));
        if (filter.getEndDate() != null) {
            subQuery = subQuery.where(subStyleRanking.crawledDate.loe(filter.getEndDate()));
        }
        return subQuery;
    }


    @Override
    public List<Tuple> getPriceInfo(BooleanBuilder builder, List<StyleId> styleIdList, StyleFilterDTO filterDTO) {
        JPQLQuery<LocalDate> latestCrawledDateSubQuery = createLatestCrawledDateSubQuery(filterDTO);
        return jpaQueryFactory.select(
                        styleRanking.styleId,
                        styleRanking.brand,
                        styleRanking.styleName,
                        styleRanking.fixedPrice,
                        styleRanking.discountedPrice,
                        styleRanking.monetaryUnit,
                        category,
                        image
                )
                .from(styleRanking)
                .innerJoin(styleRanking.categoryStyle, categoryStyle)
                .innerJoin(categoryStyle.style, style)
                .leftJoin(style.images, image)
                .where(style.id.in(styleIdList)
                        .and(builder)
                        .and(styleRanking.crawledDate.eq(latestCrawledDateSubQuery))
                        .and(image.sequence.eq(0).or(image.isNull())))
                .groupBy(styleRanking.styleId,
                        styleRanking.mallTypeId,
                        styleRanking.categoryId)
                .fetch();
    }


    @Override
    public List<Tuple> getExposureIndexInfo(BooleanBuilder builder, Pageable pageable, String sortBy, String sortOrder) {
        OrderSpecifier<?> orderSpecifier = getStyleFilterOrderSpecifier(sortBy, sortOrder);

        return jpaQueryFactory.select(
                        styleRanking.styleId,
                        styleRanking.mallTypeId,
                        category,
                        styleRanking.rankScore.sum()
                )
                .from(styleRanking)
                .join(category)
                .on(styleRanking.categoryId.eq(category.categoryId))
                .where(builder)
                .groupBy(styleRanking.styleId, styleRanking.mallTypeId, styleRanking.categoryId)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public Long getFilteredStyleCount(BooleanBuilder builder) {
        return jpaQueryFactory.select(styleRanking.styleId.countDistinct())
                .from(styleRanking)
                .where(builder)
                .fetchOne();
    }

    @Override
    public OrderSpecifier<?> getStyleFilterOrderSpecifier(String sortBy, String sortOrder) {
        Order order = sortOrder.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
        OrderSpecifier<?> orderSpecifier;

        if ("exposureIndex".equals(sortBy)) {
            NumberExpression<Float> rankScoreSum = styleRanking.rankScore.sum();
            orderSpecifier = new OrderSpecifier<>(order, rankScoreSum);
        } else {
            PathBuilder<?> entityPath = new PathBuilder<>(styleRanking.getType(), "styleRanking");

            switch (sortBy) {
                case "brand":
                    return new OrderSpecifier<>(order, entityPath.getString(sortBy));
                case "crawledDate":
                    return new OrderSpecifier<>(order, entityPath.getDateTime(sortBy, Date.class));
                case "fixedPrice", "discountedPrice":
                    return new OrderSpecifier<>(order, entityPath.getNumber(sortBy, Integer.class));
                default:
                    throw new IllegalArgumentException("Invalid sortBy parameter");
            }
        }
        return orderSpecifier;
    }


    @Override
    public BooleanBuilder buildStyleFilter(StyleFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterDTO.getBrand() != null && !filterDTO.getBrand().isEmpty()) {
            builder.and(styleRanking.brand.in(filterDTO.getBrand()));
        }

        if (filterDTO.getStartDate() != null) {
            builder.and(styleRanking.crawledDate.goe(filterDTO.getStartDate()));
        }

        if (filterDTO.getEndDate() != null) {
            builder.and(styleRanking.crawledDate.loe(filterDTO.getEndDate()));
        }

        if (filterDTO.getMallTypeId() != null && !filterDTO.getMallTypeId().isEmpty()) {
            builder.and(styleRanking.mallTypeId.eq(filterDTO.getMallTypeId()));
        }

        if (filterDTO.getCategory() != null && !filterDTO.getCategory().isEmpty()) {
            // 카테고리 필터링 로직
            builder.and(
                    styleRanking.categoryStyle.id.in(
                            JPAExpressions.select(categoryStyle.id)
                                    .from(categoryStyle)
                                    .join(categoryStyle.category, category)
                                    .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                                    .where(categoryClosure.id.ancestorId.in(filterDTO.getCategory()))
                    )
            );
        }

        return builder;
    }


}
