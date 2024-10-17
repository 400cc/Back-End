package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.api.home.dto.TopBrandDTO;
import Designovel.Capstone.api.home.queryDSL.HomeQueryDSL;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Service
@RequiredArgsConstructor
public class TopBrandService {

    private final HomeQueryDSL homeQueryDSL;

    /**
     * 쇼핑몰에 따른 Top 10 브랜드 처리 메서드
     * 1. 쇼핑몰에 따라 Top 10 브랜드를 노출 지수 순으로 조회
     * 2. 데이터 가공 후 반환
     * @param filter
     * @return List 형태로 반환
     */
    public List<TopBrandDTO> getTop10BrandsByMallTypeId(HomeFilterDTO filter) {
        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder builder = homeQueryDSL.buildTopBrandFilter(filter);
        List<Tuple> top10BrandOrderByExposureIndex = homeQueryDSL.getTop10BrandOrderByExposureIndex(builder, pageable);

        return top10BrandOrderByExposureIndex.stream()
                .map(tuple -> TopBrandDTO.builder()
                        .brand(tuple.get(styleRanking.brand))
                        .exposureIndexSum(tuple.get(styleRanking.rankScore.sum()))
                        .mallTypeId(tuple.get(styleRanking.mallTypeId))
                        .build())
                .collect(Collectors.toList());
    }
}
