package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.PriceRangeFilterDTO;
import Designovel.Capstone.domain.style.styleRanking.QStyleRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface PriceRangeQueryDSL {
    List<Tuple> findStyleRankingWithPriceRanges(Integer minPrice, Integer intervalSize, BooleanBuilder priceRangeFilter, JPQLQuery<LocalDate> latestCrawledDate, List<String> priceRangeKey);

    JPQLQuery<LocalDate> createLatestCrawledDateSubQuery();

    BooleanBuilder buildPriceRangeFilter(PriceRangeFilterDTO filterDTO);

    StringExpression createPriceRangeExpression(int minPrice, int intervalSize, List<String> ranges);

    List<Tuple> findMinMaxPriceByFilter(JPQLQuery<LocalDate> lastCrawledDateQuery, BooleanBuilder priceRangeFilter);
}
