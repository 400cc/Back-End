package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.domain.style.styleRanking.QStyleRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.categoryStyle;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriceRangeQueryDSLImpl implements PriceRangeQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public JPQLQuery<LocalDate> createLatestCrawledDateSubQuery() {
        QStyleRanking subStyleRanking = new QStyleRanking("subStyleRanking");
        BooleanBuilder subQueryConditions = new BooleanBuilder();
        subQueryConditions.and(subStyleRanking.categoryStyle.id.eq(styleRanking.categoryStyle.id));

        return JPAExpressions.select(subStyleRanking.crawledDate.max())
                .from(subStyleRanking)
                .where(subQueryConditions);
    }

    @Override
    public List<Tuple> findMinMaxPriceByFilter(JPQLQuery<LocalDate> lastCrawledDateQuery, BooleanBuilder priceRangeFilter) {
        return jpaQueryFactory.select(
                        styleRanking.discountedPrice.min(),
                        styleRanking.discountedPrice.max()
                )
                .from(styleRanking)
                .where(styleRanking.crawledDate.eq(lastCrawledDateQuery)
                        .and(priceRangeFilter))
                .fetch();
    }

    @Override
    public List<Tuple> findStyleRankingWithPriceRanges(Integer minPrice, Integer intervalSize, BooleanBuilder priceRangeFilter, JPQLQuery<LocalDate> latestCrawledDate, List<String> priceRangeKey) {
        StringExpression priceRangeExpression = createPriceRangeExpression(minPrice, intervalSize, priceRangeKey);

        return jpaQueryFactory.select(
                        styleRanking.count().as("count"),
                        priceRangeExpression.as("priceRange")
                )
                .from(styleRanking)
                .where(priceRangeFilter)
                .groupBy(priceRangeExpression)
                .orderBy(styleRanking.discountedPrice.asc())
                .fetch();
    }


    @Override
    public StringExpression createPriceRangeExpression(int minPrice, int intervalSize, List<String> ranges) {
        CaseBuilder.Cases<String, StringExpression> caseBuilder = new CaseBuilder()
                .when(styleRanking.discountedPrice.between(minPrice, minPrice + intervalSize - 1)).then(ranges.get(0));
        for (int i = 1; i < ranges.size(); i++) {
            int lowerBound = minPrice + i * intervalSize;
            int upperBound = lowerBound + intervalSize;
            caseBuilder = caseBuilder.when(styleRanking.discountedPrice.between(lowerBound, upperBound)).then(ranges.get(i));
        }

        return caseBuilder.otherwise("");
    }


    @Override
    public BooleanBuilder buildPriceRangeFilter(HomeFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterDTO.getMallTypeId() != null && !filterDTO.getMallTypeId().isEmpty()) {
            builder.and(styleRanking.categoryStyle.id.mallTypeId.eq(filterDTO.getMallTypeId()));
        }

        if (filterDTO.getStartDate() != null) {
            builder.and(styleRanking.crawledDate.goe(filterDTO.getStartDate()));
        }

        if (filterDTO.getEndDate() != null) {
            builder.and(styleRanking.crawledDate.loe(filterDTO.getEndDate()));
        } else {
            JPQLQuery<LocalDate> latestCrawledDateSubQuery = createLatestCrawledDateSubQuery();
            builder.and(styleRanking.crawledDate.eq(latestCrawledDateSubQuery));
        }

        if (filterDTO.getCategory() != null && !filterDTO.getCategory().isEmpty()) {
            // 카테고리 필터링 로직
            builder.and(
                    styleRanking.categoryStyle.id.styleId.in(
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

