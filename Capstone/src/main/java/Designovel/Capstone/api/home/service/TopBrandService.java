package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.TopBrandDTO;
import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.api.home.queryDSL.HomeQueryDSL;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.categoryStyle;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Service
@RequiredArgsConstructor
public class TopBrandService {

    private final HomeQueryDSL homeQueryDSL;

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
