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
    public JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(HomeFilterDTO filter) {
        QStyleRanking subStyleRanking = new QStyleRanking("subStyleRanking");
        JPQLQuery<LocalDate> subQuery = JPAExpressions.select(subStyleRanking.crawledDate.max())
                .from(subStyleRanking)
                .where(subStyleRanking.categoryStyle.id.eq(styleRanking.categoryStyle.id));

        if (filter.getEndDate() != null) {
            subQuery = subQuery.where(subStyleRanking.crawledDate.loe(filter.getEndDate()));
        }
        return subQuery;
    }

    @Override
    public List<Tuple> findMinMaxPriceByFilter(BooleanBuilder priceRangeFilter) {
        return jpaQueryFactory.select(
                        styleRanking.discountedPrice.min(),
                        styleRanking.discountedPrice.max()
                )
                .from(styleRanking)
                .where(priceRangeFilter)
                .fetch();
    }

    @Override
    public List<Tuple> findStyleRankingWithPriceRanges(Integer minPrice, Integer intervalSize, BooleanBuilder priceRangeFilter, List<String> priceRangeKey, HomeFilterDTO filterDTO) {
        StringExpression priceRangeExpression = createPriceRangeExpression(minPrice, intervalSize, priceRangeKey);
        JPQLQuery<LocalDate> latestCrawledDateSubQuery = createLatestCrawledDateSubQuery(filterDTO);

        return jpaQueryFactory.select(
                        styleRanking.count().as("count"),
                        priceRangeExpression.as("priceRange")
                )
                .from(styleRanking)
                .where(priceRangeFilter.and(styleRanking.crawledDate.eq(latestCrawledDateSubQuery)))
                .groupBy(priceRangeExpression)
                .orderBy(styleRanking.discountedPrice.asc())
                .fetch();
    }


    @Override
    public StringExpression createPriceRangeExpression(int minPrice, int intervalSize, List<String> ranges) {
        CaseBuilder.Cases<String, StringExpression> caseBuilder = new CaseBuilder()
                .when(styleRanking.discountedPrice.between(minPrice, minPrice + intervalSize - 1)).then(ranges.get(0));
        for (int interval = 1; interval < ranges.size(); interval++) {
            int lowerBound = minPrice + (interval * intervalSize);
            int upperBound = lowerBound + intervalSize;
            caseBuilder = caseBuilder.when(styleRanking.discountedPrice.between(lowerBound, upperBound)).then(ranges.get(interval));
        }

        return caseBuilder.otherwise("");
    }


    @Override
    public BooleanBuilder buildPriceRangeFilter(HomeFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        if (filterDTO.getMallTypeId() != null && !filterDTO.getMallTypeId().isEmpty()) {
            builder.and(styleRanking.categoryStyle.id.mallTypeId.eq(filterDTO.getMallTypeId()));
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

        if (filterDTO.getStartDate() != null) {
            builder.and(styleRanking.crawledDate.goe(filterDTO.getStartDate()));
        }

//        JPQLQuery<LocalDate> latestCrawledDateSubQuery = createLatestCrawledDateSubQuery(filterDTO);
//        builder.and(styleRanking.crawledDate.eq(latestCrawledDateSubQuery));

        return builder;
    }
}

