package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.productFilter.dto.ProductFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface HomeQueryDSL {
    List<Tuple> getTop10BrandOrderByExposureIndex(BooleanBuilder builder, Pageable pageable);

}