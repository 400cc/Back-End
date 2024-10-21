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
//임의로 정한 구간에서 가격대별 상품을 찾는 클래스
public class NewPriceRangeService {

    private final PriceRangeRepository priceRangeQueryDSL;

    // 가격 범위를 상수로 정의
    private static final int[] PRICE_BOUNDARIES = {9999, 29999, 49999, 99999, 199999, 499999, 999999, 1999999, 4999999, 9999999};

    /**
     * PRICE_BOUNDARIES를 이용하여 가격대별 상품 수 계산하는 메서드
     *
     * @param filterDTO 필터 조건
     * @return 가격 범위에 따른 스타일 개수
     */
    public Map<String, Integer> getPriceRangesCountList(HomeFilterDTO filterDTO) {
        //DB 조회
        List<Integer> discountedPriceList = priceRangeQueryDSL.findDiscountedPriceByFilter(filterDTO);
        if (discountedPriceList.isEmpty()) {
            return Collections.singletonMap("0-0", 0);
        }

        //Map 만들기
        Map<String, Integer> priceRangeMap = initializePriceRangeMap();
        //가격대별 상품 수 계산
        countPriceRanges(priceRangeMap, discountedPriceList);

        return priceRangeMap;
    }

    /**
     * Map Key를 초기화 하는 메서드
     * @return
     */
    private Map<String, Integer> initializePriceRangeMap() {
        Map<String, Integer> priceRangeMap = new LinkedHashMap<>();
        for (int i = 0; i < PRICE_BOUNDARIES.length; i++) {
            String rangeKey = createPriceRangeKey(i);
            priceRangeMap.put(rangeKey, 0);
        }
        //마지막 범위(10000000+)
        priceRangeMap.put((PRICE_BOUNDARIES[PRICE_BOUNDARIES.length - 1] + 1) + "+", 0);
        return priceRangeMap;
    }

    /**
     * PRICE_BOUNDARIES를 이용하여 Key를 동적으로 생성
     * - 형식: 가격-가격(ex. 0-9999, 10000-29999)
     * - 마지막 가격은 마지막 값 + 1(ex. 10000000+)
     * @param index
     * @return
     */
    private String createPriceRangeKey(int index) {
        if (index == 0) {
            return "0-" + PRICE_BOUNDARIES[index];
        } else if (index == PRICE_BOUNDARIES.length) {
            // 마지막 범위 처리 (PRICE_BOUNDARIES.length가 반환되었을 때)
            return (PRICE_BOUNDARIES[PRICE_BOUNDARIES.length - 1] + 1) + "+";
        } else {
            return (PRICE_BOUNDARIES[index - 1] + 1) + "-" + PRICE_BOUNDARIES[index];
        }
    }

    /**
     * Map의 Key에 맞는 가격을 찾고 값을 증가하는 메서드
     * @param priceRangeMap
     * @param discountedPriceList
     */
    private void countPriceRanges(Map<String, Integer> priceRangeMap, List<Integer> discountedPriceList) {
        for (int price : discountedPriceList) {
            int rangeIndex = getRangeIndex(price);
            String rangeKey = createPriceRangeKey(rangeIndex);
            priceRangeMap.put(rangeKey, priceRangeMap.get(rangeKey) + 1);
        }
    }

    /**
     * 해당 가격의 Key를 찾는 메서드
     * @param price
     * @return
     */
    private int getRangeIndex(int price) {
        if (price <= PRICE_BOUNDARIES[0]) return 0;
        for (int i = 1; i < PRICE_BOUNDARIES.length; i++) {
            if (price <= PRICE_BOUNDARIES[i]) {
                return i;
            }
        }
        return PRICE_BOUNDARIES.length; // 마지막 범위 처리
    }
}
