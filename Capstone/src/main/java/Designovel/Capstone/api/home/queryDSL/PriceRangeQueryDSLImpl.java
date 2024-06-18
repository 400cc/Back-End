package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.domain.style.styleRanking.QStyleRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriceRangeQueryDSLImpl implements PriceRangeQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;


    private JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(QStyleRanking subStyleRanking) {
        BooleanBuilder subQueryConditions = new BooleanBuilder();
        subQueryConditions.and(subStyleRanking.categoryStyle.id.eq(styleRanking.categoryStyle.id));

        return JPAExpressions.select(subStyleRanking.crawledDate.max())
                .from(subStyleRanking)
                .where(subQueryConditions);
    }

    public List<Tuple> findStyleRankingWithPriceRanges(Integer minPrice, Integer maxPrice, Pageable pageable) {
        int range = maxPrice - minPrice;
        int intervalSize = (int) Math.ceil(range / 10.0 / 1000) * 1000;
        QStyleRanking styleRanking = QStyleRanking.styleRanking;

        // Define the case statements
        String[] ranges = {
                "Range 1", "Range 2", "Range 3", "Range 4", "Range 5",
                "Range 6", "Range 7", "Range 8", "Range 9", "Range 10"
        };

        // Latest crawled date subquery
        QStyleRanking subStyleRanking = new QStyleRanking("subStyleRanking");
        JPQLQuery<LocalDate> latestCrawledDate = createLatestCrawledDateSubQuery(subStyleRanking);

        // Collect results for each range
        List<Tuple> results = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int lowerBound = minPrice + i * intervalSize;
            int upperBound = (i == 9) ? maxPrice : lowerBound + intervalSize - 1;

            List<Tuple> rangeResults = jpaQueryFactory.select(
                            styleRanking.rankId,
                            styleRanking.categoryStyle,
                            styleRanking.brand,
                            styleRanking.rankScore,
                            styleRanking.styleName,
                            styleRanking.fixedPrice,
                            styleRanking.discountedPrice,
                            styleRanking.monetaryUnit,
                            styleRanking.crawledDate,
                            new CaseBuilder()
                                    .when(styleRanking.fixedPrice.between(lowerBound, upperBound)).then(ranges[i])
                                    .otherwise("Out of Range").as("priceRange")
                    )
                    .from(styleRanking)
                    .where(styleRanking.crawledDate.eq(latestCrawledDate)
                            .and(styleRanking.fixedPrice.between(lowerBound, upperBound)))
                    .orderBy(styleRanking.fixedPrice.asc())
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            results.addAll(rangeResults);
        }

        return results;
    }
}

