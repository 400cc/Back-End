package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.PriceRangeFilterDTO;
import Designovel.Capstone.api.home.queryDSL.PriceRangeQueryDSLImpl;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceRangeService {

    private final StyleRankingRepository styleRankingRepository;
    private final PriceRangeQueryDSLImpl priceRangeQueryDSL;

    public Map<String, Integer> getStyleByPriceRange(PriceRangeFilterDTO priceRangeFilterDTO) {
        List<Object[]> minMaxPriceList = styleRankingRepository.findMinMaxFixedPriceByMallTypeId(priceRangeFilterDTO.getMallTypeId());

        Object[] minMaxPrice = minMaxPriceList.get(0);
        Integer minPrice = ((Number) minMaxPrice[0]).intValue();
        Integer maxPrice = ((Number) minMaxPrice[1]).intValue();

        int range = maxPrice - minPrice;
        int intervalSize = (int) Math.ceil((double) range / 10.0 / 1000) * 1000;

        List<String> priceRangeKey = createPriceRangeKeys(minPrice, intervalSize);
        Map<String, Integer> priceRangeMap = createPriceRangeMap(priceRangeKey);
        List<Tuple> results = priceRangeQueryDSL.findStyleRankingWithPriceRanges(minPrice, intervalSize, priceRangeFilterDTO, priceRangeKey);

        for (Tuple tuple : results) {
            Long countLong = tuple.get(0, Long.class);
            Integer count = countLong != null ? countLong.intValue() : null; // Long 값을 Integer로 변환
            String priceRange = tuple.get(1, String.class);
            priceRangeMap.put(priceRange, count);
        }
        return priceRangeMap;
    }

    private static Map<String, Integer> createPriceRangeMap(List<String> priceRangeKey) {
        return priceRangeKey.stream()
                .collect(Collectors.toMap(
                        priceRange -> priceRange,
                        priceRange -> 0,
                        (e1, e2) -> e1,
                        LinkedHashMap::new
                ));
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
