package Designovel.Capstone.domain.style.styleRanking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class StyleRankingService {

    private final StyleRankingRepository styleRankingRepository;

    public List<String> getBrandsByMallTypeId(String mallTypeId) {
        return styleRankingRepository.findDistinctBrand(mallTypeId);
    }

    public Map<String, Float> getMinMaxFixedPriceByMallTypeId(String mallTypeId) {
        Map<String, Float> minMaxFixedPriceMap = new HashMap<>();
        Object[] queryResult = styleRankingRepository.findMinMaxFixedPriceByMallTypeId(mallTypeId);
        Float minFixedPrice = (Float) queryResult[0];
        Float maxFixedPrice = (Float) queryResult[1];
        minMaxFixedPriceMap.put("minFixedPrice", minFixedPrice);
        minMaxFixedPriceMap.put("maxFixedPrice", maxFixedPrice);
        return minMaxFixedPriceMap;
    }
}
