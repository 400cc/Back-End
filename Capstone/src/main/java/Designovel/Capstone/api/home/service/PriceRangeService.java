package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.api.home.queryDSL.PriceRangeQueryDSLImpl;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceRangeService {

    private final PriceRangeQueryDSLImpl priceRangeQueryDSL;

    public Map<String, Integer> getPriceRangesCountList(HomeFilterDTO filterDTO) {
        BooleanBuilder priceRangeFilter = priceRangeQueryDSL.buildPriceRangeFilter(filterDTO);
        Tuple minMaxPriceByFilter = priceRangeQueryDSL.findMinMaxPriceByFilter(priceRangeFilter).get(0);

        Integer minPrice = Optional.ofNullable(minMaxPriceByFilter.get(0, Integer.class)).orElse(0);
        Integer maxPrice = Optional.ofNullable(minMaxPriceByFilter.get(1, Integer.class)).orElse(0);

        if (minPrice.equals(0) && maxPrice.equals(0)) {
            // 값이 없을 경우
            return Collections.singletonMap("0-0", 0);
        }

        int intervalSize = calculateIntervalSize(maxPrice, minPrice);

        List<String> priceRangeKey = createPriceRangeKeys(minPrice, maxPrice, intervalSize);
        Map<String, Integer> priceRangeMap = createPriceRangeMap(priceRangeKey);
        List<Tuple> results = priceRangeQueryDSL.findStyleRankingWithPriceRanges(minPrice, intervalSize, priceRangeFilter, priceRangeKey);
        mapPriceRangesWithStyleCount(priceRangeMap, results);
        return priceRangeMap;
    }

    public int calculateIntervalSize(Integer maxPrice, Integer minPrice) {
        int range = maxPrice - minPrice;
        return (int) Math.ceil((double) range / 10.0);
    }

    public Map<String, Integer> createPriceRangeMap(List<String> priceRangeKey) {
        return priceRangeKey.stream()
                .collect(Collectors.toMap(
                        priceRange -> priceRange,
                        priceRange -> 0,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
    }


    public void mapPriceRangesWithStyleCount(Map<String, Integer> priceRangeMap, List<Tuple> results) {
        for (Tuple tuple : results) {
            Integer styleCount = Optional.ofNullable(tuple.get(0, Long.class)).map(Long::intValue).orElse(0);
            String priceRange = tuple.get(1, String.class);
            priceRangeMap.put(priceRange, styleCount);
        }
    }

    public List<String> createPriceRangeKeys(int minPrice, int maxPrice, int intervalSize) {
        List<String> ranges = new ArrayList<>();
        for (int interval = 0; interval < 10; interval++) {
            int lowerBound = minPrice + (interval * intervalSize);
            int upperBound = (interval == 9) ? maxPrice : lowerBound + intervalSize - 1;
            ranges.add(lowerBound + "-" + upperBound);
        }
        return ranges;
    }

}
