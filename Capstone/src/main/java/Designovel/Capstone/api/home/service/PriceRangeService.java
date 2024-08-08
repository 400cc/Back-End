package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.api.home.queryDSL.PriceRangeQueryDSLImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceRangeService {

    private final PriceRangeQueryDSLImpl priceRangeQueryDSL;

    public Map<String, Integer> getPriceRangesCountList(HomeFilterDTO filterDTO) {

        List<Integer> discountedPriceList = priceRangeQueryDSL.findDiscountedPriceByFilter(filterDTO);
        Integer minPrice = discountedPriceList.stream().mapToInt(Integer::intValue).min().orElse(0);
        Integer maxPrice = discountedPriceList.stream().mapToInt(Integer::intValue).max().orElse(0);
        Integer intervalSize = calculateIntervalSize(maxPrice, minPrice);

        List<String> priceRangeKey = createPriceRangeKeys(minPrice, maxPrice, intervalSize);
        Map<String, Integer> priceRangeMap = createPriceRangeMap(priceRangeKey);
        mapPriceRangesWithStyleCount(priceRangeMap, discountedPriceList);
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


    public void mapPriceRangesWithStyleCount(Map<String, Integer> priceRangeMap, List<Integer> discountedPriceList) {
        for (int price : discountedPriceList) {
            for (String range : priceRangeMap.keySet()) {
                String[] bounds = range.split("-");
                int lowerBound = Integer.parseInt(bounds[0]);
                int upperBound = Integer.parseInt(bounds[1]);

                if (price >= lowerBound && price <= upperBound) {
                    priceRangeMap.put(range, priceRangeMap.get(range) + 1);
                    break;
                }
            }
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
