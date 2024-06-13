package Designovel.Capstone.api.productFilter.queryDSL;

import Designovel.Capstone.api.productFilter.dto.StyleFilterDTO;
import Designovel.Capstone.domain.product.product.ProductId;
import Designovel.Capstone.domain.product.productRanking.QProductRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
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
import static Designovel.Capstone.domain.category.categoryProduct.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.domain.product.productRanking.QProductRanking.productRanking;

@Slf4j
@Repository
@RequiredArgsConstructor
public class StyleFilterQueryDSLImpl implements StyleFilterQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;

    private JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(QProductRanking subProductRanking) {
        BooleanBuilder subQueryConditions = new BooleanBuilder();
        subQueryConditions.and(subProductRanking.categoryProduct.id.eq(productRanking.categoryProduct.id));

        return JPAExpressions.select(subProductRanking.crawledDate.max())
                .from(subProductRanking)
                .where(subQueryConditions);
    }


    @Override
    public List<Tuple> getPriceInfo(BooleanBuilder builder, List<ProductId> productIdList) {
        QProductRanking subProductRanking = new QProductRanking("subProductRanking");
        JPQLQuery<LocalDate> latestCrawledDateSubQuery = createLatestCrawledDateSubQuery(subProductRanking);

        return jpaQueryFactory.select(
                        categoryProduct.product,
                        productRanking.fixedPrice,
                        productRanking.discountedPrice,
                        productRanking.monetaryUnit,
                        categoryProduct.category
                )
                .from(productRanking)
                .leftJoin(productRanking.categoryProduct, categoryProduct)
                .where(builder.and(productRanking.crawledDate.eq(latestCrawledDateSubQuery))
                        .and(categoryProduct.product.id.in(productIdList)))
                .groupBy(categoryProduct.product.id.productId,
                        categoryProduct.product.id.mallTypeId,
                        categoryProduct.category)
                .fetch();
    }


    @Override
    public QueryResults<Tuple> getExposureIndexInfo(BooleanBuilder builder, Pageable pageable, String sortBy, String sortOrder) {
        OrderSpecifier<?> orderSpecifier = getProductFilterOrderSpecifier(sortBy, sortOrder);
        return jpaQueryFactory.select(
                        categoryProduct.product,
                        productRanking.brand,
                        productRanking.rankScore.sum(),
                        categoryProduct.category
                )
                .from(productRanking)
                .leftJoin(productRanking.categoryProduct, categoryProduct)
                .where(builder)
                .groupBy(categoryProduct.product.id.productId,
                        categoryProduct.product.id.mallTypeId,
                        categoryProduct.category.name)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
    }

    @Override
    public OrderSpecifier<?> getProductFilterOrderSpecifier(String sortBy, String sortOrder) {
        Order order = sortOrder.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
        OrderSpecifier<?> orderSpecifier;

        if ("exposureIndex".equals(sortBy)) {
            NumberExpression<Float> rankScoreSum = productRanking.rankScore.sum();
            orderSpecifier = new OrderSpecifier<>(order, rankScoreSum);
        } else {
            PathBuilder<?> entityPath = new PathBuilder<>(productRanking.getType(), "productRanking");

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
    public BooleanBuilder buildProductFilter(StyleFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterDTO.getBrand() != null) {
            builder.and(productRanking.brand.in(filterDTO.getBrand()));
        }

        if (filterDTO.getStartDate() != null) {
            builder.and(productRanking.crawledDate.goe(filterDTO.getStartDate()));
        }

        if (filterDTO.getEndDate() != null) {
            builder.and(productRanking.crawledDate.loe(filterDTO.getEndDate()));
        }

        if (filterDTO.getMallTypeId() != null) {
            builder.and(productRanking.categoryProduct.id.mallTypeId.eq(filterDTO.getMallTypeId()));
        }

        if (filterDTO.getCategory() != null && !filterDTO.getCategory().isEmpty()) {
            // 카테고리 필터링 로직
            builder.and(
                    productRanking.categoryProduct.id.productId.in(
                            JPAExpressions.select(categoryProduct.id.productId)
                                    .from(categoryProduct)
                                    .join(categoryProduct.category, category)
                                    .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                                    .where(categoryClosure.id.ancestorId.in(filterDTO.getCategory()))
                    )
            );
        }

        return builder;
    }


}
