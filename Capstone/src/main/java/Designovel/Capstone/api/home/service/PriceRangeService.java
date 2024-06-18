package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.PriceRangeDTO;
import Designovel.Capstone.api.home.queryDSL.PriceRangeQueryDSLImpl;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Service
@RequiredArgsConstructor
@Slf4j
public class PriceRangeService {

    private final StyleRankingRepository styleRankingRepository;
    private final PriceRangeQueryDSLImpl priceRangeQueryDSL;

    public Map<String, List<PriceRangeDTO>> getStyleByPriceRange(String mallTypeId, int page) {
        List<Object[]> minMaxPriceList = styleRankingRepository.findMinMaxFixedPriceByMallTypeId(mallTypeId);
        Object[] minMaxPrice = minMaxPriceList.get(0);
        Integer minPrice = ((Number) minMaxPrice[0]).intValue();
        Integer maxPrice = ((Number) minMaxPrice[1]).intValue();
        Pageable pageable = PageRequest.of(page, 10);
        List<Tuple> results = priceRangeQueryDSL.findStyleRankingWithPriceRanges(minPrice, maxPrice, pageable);

        Map<String, List<PriceRangeDTO>> priceRangeMap = new LinkedHashMap<>();
        for (Tuple tuple : results) {
            Integer fixedPrice = tuple.get(styleRanking.fixedPrice);
            PriceRangeDTO priceRangeDTO = PriceRangeDTO.builder()
                    .rankId(tuple.get(styleRanking.rankId))
                    .categoryStyle(tuple.get(styleRanking.categoryStyle))
                    .brand(tuple.get(styleRanking.brand))
                    .rankScore(tuple.get(styleRanking.rankScore))
                    .styleName(tuple.get(styleRanking.styleName))
                    .fixedPrice(tuple.get(styleRanking.fixedPrice))
                    .discountedPrice(tuple.get(styleRanking.discountedPrice))
                    .monetaryUnit(tuple.get(styleRanking.monetaryUnit))
                    .crawledDate(tuple.get(styleRanking.crawledDate)).build();

            // 구간 계산 (1000 단위)
            String rangeKey = generateRangeKey(minPrice, maxPrice, fixedPrice);
            priceRangeMap.computeIfAbsent(rangeKey, k -> new ArrayList<>()).add(priceRangeDTO);
        }

        return priceRangeMap;
    }

    private static String generateRangeKey(Integer minPrice, Integer maxPrice, Integer fixedPrice) {
        int intervalSize = (int) Math.ceil((maxPrice - minPrice) / 10.0 / 1000) * 1000;
        int rangeStart = (fixedPrice - minPrice) / intervalSize * intervalSize + minPrice;
        int rangeEnd = rangeStart + intervalSize - 1;
        String rangeKey = rangeStart + "-" + rangeEnd;
        return rangeKey;
    }
}
