package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.api.home.queryDSL.PriceRangeQueryDSLImpl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPQLQuery;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceRangeService {

    private final PriceRangeQueryDSLImpl priceRangeQueryDSL;

    private static Map<String, Integer> createPriceRangeMap(List<String> priceRangeKey) {
        return priceRangeKey.stream()
                .collect(Collectors.toMap(
                        priceRange -> priceRange,
                        priceRange -> 0,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }

    public Map<String, Integer> getPriceRangesCountList(HomeFilterDTO filterDTO) {
        JPQLQuery<LocalDate> latestCrawledDate = priceRangeQueryDSL.createLatestCrawledDateSubQuery();
        BooleanBuilder priceRangeFilter = priceRangeQueryDSL.buildPriceRangeFilter(filterDTO);
        Tuple minMaxPriceByFilter = priceRangeQueryDSL.findMinMaxPriceByFilter(latestCrawledDate, priceRangeFilter).get(0);

        Integer minPrice = minMaxPriceByFilter.get(0, Integer.class) != null ? minMaxPriceByFilter.get(0, Integer.class) : 0;
        Integer maxPrice = minMaxPriceByFilter.get(1, Integer.class) != null ? minMaxPriceByFilter.get(1, Integer.class) : 0;

        int range = maxPrice - minPrice;
        int intervalSize = (int) Math.ceil((double) range / 10.0 / 1000) * 1000;

        List<String> priceRangeKey = createPriceRangeKeys(minPrice, intervalSize);
        Map<String, Integer> priceRangeMap = createPriceRangeMap(priceRangeKey);
        List<Tuple> results = priceRangeQueryDSL.findStyleRankingWithPriceRanges(minPrice, intervalSize, priceRangeFilter, latestCrawledDate, priceRangeKey);

        for (Tuple tuple : results) {
            Long countLong = tuple.get(0, Long.class);
            Integer count = countLong != null ? countLong.intValue() : null; // Long 값을 Integer로 변환
            String priceRange = tuple.get(1, String.class);
            priceRangeMap.put(priceRange, count);
        }
        return priceRangeMap;
    }

    public List<String> createPriceRangeKeys(int minPrice, int intervalSize) {
        List<String> ranges = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int lowerBound = minPrice + i * intervalSize;
            int upperBound = lowerBound + intervalSize;
            ranges.add(lowerBound + "-" + upperBound);
        }
        return ranges;
    }

}
