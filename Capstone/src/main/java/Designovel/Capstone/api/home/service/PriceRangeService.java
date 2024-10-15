package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.api.home.queryDSL.PriceRangeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceRangeService {

    private final PriceRangeRepository priceRangeQueryDSL;

    public Map<String, Integer> getPriceRangesCountList(HomeFilterDTO filterDTO) {

        List<Integer> discountedPriceList = priceRangeQueryDSL.findDiscountedPriceByFilter(filterDTO);
        if (discountedPriceList.isEmpty()) {
            return Collections.singletonMap("0-0", 0);
        }
        IntSummaryStatistics stats = discountedPriceList.stream().mapToInt(Integer::intValue).summaryStatistics();
        int minPrice = stats.getMin();
        int maxPrice = stats.getMax();
        int intervalSize = calculateIntervalSize(maxPrice, minPrice);

        List<String> priceRangeKey = createPriceRangeKeys(minPrice, maxPrice, intervalSize);
        Map<String, Integer> priceRangeMap = createPriceRangeMap(priceRangeKey);
        mapPriceRangesWithStyleCount(priceRangeMap, discountedPriceList, minPrice, intervalSize);
        return priceRangeMap;
    }

    private int calculateIntervalSize(Integer maxPrice, Integer minPrice) {
        int range = maxPrice - minPrice;
        return (int) Math.ceil((double) range / 10.0);
    }

    private Map<String, Integer> createPriceRangeMap(List<String> priceRangeKeys) {
        Map<String, Integer> priceRangeMap = new LinkedHashMap<>();
        for (String key : priceRangeKeys) {
            priceRangeMap.put(key, 0);
        }
        return priceRangeMap;
    }


    public void mapPriceRangesWithStyleCount(Map<String, Integer> priceRangeMap, List<Integer> discountedPriceList, int minPrice, int intervalSize) {
        List<String> keys = new ArrayList<>(priceRangeMap.keySet());
        for (int price : discountedPriceList) {
            int rangeIndex = Math.min((price - minPrice) / intervalSize, keys.size() - 1); //최대값일 경우 index out of bound 방지
            String rangeKey = keys.get(rangeIndex);
            priceRangeMap.put(rangeKey, priceRangeMap.get(rangeKey) + 1);
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
