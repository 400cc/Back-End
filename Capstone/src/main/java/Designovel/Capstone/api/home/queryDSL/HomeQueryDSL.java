package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeQueryDSL {
    //Top10 브랜드 노출 지수 순으로 조회 메서드
    List<Tuple> getTop10BrandOrderByExposureIndex(BooleanBuilder builder, Pageable pageable);

    //동적 필터 생성 메서드
    BooleanBuilder buildTopBrandFilter(HomeFilterDTO filterDTO);
}
