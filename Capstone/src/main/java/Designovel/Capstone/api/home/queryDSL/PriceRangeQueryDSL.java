package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;

import java.time.LocalDate;
import java.util.List;

public interface PriceRangeQueryDSL {
    List<Tuple> findStyleRankingWithPriceRanges(Integer minPrice, Integer intervalSize, BooleanBuilder priceRangeFilter, JPQLQuery<LocalDate> latestCrawledDate, List<String> priceRangeKey);

    JPQLQuery<LocalDate> createLatestCrawledDateSubQuery();

    BooleanBuilder buildPriceRangeFilter(HomeFilterDTO filterDTO);

    StringExpression createPriceRangeExpression(int minPrice, int intervalSize, List<String> ranges);

    List<Tuple> findMinMaxPriceByFilter(JPQLQuery<LocalDate> lastCrawledDateQuery, BooleanBuilder priceRangeFilter);
}
