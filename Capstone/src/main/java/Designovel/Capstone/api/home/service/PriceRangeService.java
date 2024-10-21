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
//이전 10개의 구간에서 가격대별 상품을 찾는 클래스
public class PriceRangeService {

    private final PriceRangeRepository priceRangeQueryDSL;

    /**
     * 가격대 별 상품 수 처리 메서드
     * 1. 가격대 별 상품 수 DB 조회
     * 2. X축을 10개의 구간으로 나누어 데이터 가공
     * 3. 데이터 반환
     * @param filterDTO
     * @return 구간(ex. 1000-2000)을 Key로하고 상품 수를 Value로 하는 Map 반환
     */
    public Map<String, Integer> getPriceRangesCountList(HomeFilterDTO filterDTO) {

        //DB 조회
        List<Integer> discountedPriceList = priceRangeQueryDSL.findDiscountedPriceByFilter(filterDTO);
        if (discountedPriceList.isEmpty()) {
            return Collections.singletonMap("0-0", 0);
        }

        //최소값, 최대값을 찾아 X축을 10개의 구간으로 나눔
        IntSummaryStatistics stats = discountedPriceList.stream().mapToInt(Integer::intValue).summaryStatistics();
        int minPrice = stats.getMin();
        int maxPrice = stats.getMax();
        int intervalSize = calculateIntervalSize(maxPrice, minPrice);

        //나눈 10개의 구간을 이용하여 Key 값 생성 및 값 할당
        List<String> priceRangeKey = createPriceRangeKeys(minPrice, maxPrice, intervalSize);
        Map<String, Integer> priceRangeMap = createPriceRangeMap(priceRangeKey);
        mapPriceRangesWithStyleCount(priceRangeMap, discountedPriceList, minPrice, intervalSize);
        return priceRangeMap;
    }

    /**
     * 최소값과 최대값을 이용하여 10개의 구간으로 나눈 간격 반환
     * @param maxPrice
     * @param minPrice
     * @return 10개의 구간으로 나눈 간격 반환
     */
    private int calculateIntervalSize(Integer maxPrice, Integer minPrice) {
        int range = maxPrice - minPrice;
        return (int) Math.ceil((double) range / 10.0);
    }

    /**
     * 최소값과 최대값, 간격을 이용하여 Key(ex. 1000-2000) 생성 후 반환
     * @param minPrice
     * @param maxPrice
     * @param intervalSize
     * @return Key가 들어 있는 List 반환
     */
    public List<String> createPriceRangeKeys(int minPrice, int maxPrice, int intervalSize) {
        List<String> ranges = new ArrayList<>();
        for (int interval = 0; interval < 10; interval++) {
            int lowerBound = minPrice + (interval * intervalSize);
            int upperBound = (interval == 9) ? maxPrice : lowerBound + intervalSize - 1;
            ranges.add(lowerBound + "-" + upperBound);
        }
        return ranges;
    }


    /**
     * 생성된 Key를 바탕으로 Map 생성 후 반환
     * @param priceRangeKeys
     * @return 생성된 Key를 바탕으로 Map 반환
     */
    private Map<String, Integer> createPriceRangeMap(List<String> priceRangeKeys) {
        Map<String, Integer> priceRangeMap = new LinkedHashMap<>();
        for (String key : priceRangeKeys) {
            priceRangeMap.put(key, 0);
        }
        return priceRangeMap;
    }


    /**
     * 생성된 Map에 Key에 맞는 상품 수 할당
     * @param priceRangeMap
     * @param discountedPriceList
     * @param minPrice
     * @param intervalSize
     */
    public void mapPriceRangesWithStyleCount(Map<String, Integer> priceRangeMap, List<Integer> discountedPriceList, int minPrice, int intervalSize) {
        List<String> keys = new ArrayList<>(priceRangeMap.keySet());
        for (int price : discountedPriceList) {
            int rangeIndex = Math.min((price - minPrice) / intervalSize, keys.size() - 1); //최대값일 경우 index out of bound 방지
            String rangeKey = keys.get(rangeIndex);
            priceRangeMap.put(rangeKey, priceRangeMap.get(rangeKey) + 1);
        }
    }


}
