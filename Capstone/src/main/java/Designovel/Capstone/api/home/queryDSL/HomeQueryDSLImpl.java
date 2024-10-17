package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.categoryStyle;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeQueryDSLImpl implements HomeQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> getTop10BrandOrderByExposureIndex(BooleanBuilder builder, Pageable pageable) {
        return jpaQueryFactory.select(
                        styleRanking.brand,
                        styleRanking.rankScore.sum(),
                        styleRanking.mallTypeId
                )
                .from(styleRanking)
                .where(builder)
                .groupBy(styleRanking.brand,
                        styleRanking.mallTypeId)
                .orderBy(styleRanking.rankScore.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public BooleanBuilder buildTopBrandFilter(HomeFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();

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
                    styleRanking.styleId.in(
                            JPAExpressions.select(categoryStyle.id.styleId)
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
