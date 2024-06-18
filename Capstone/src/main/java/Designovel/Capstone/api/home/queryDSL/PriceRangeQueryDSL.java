package Designovel.Capstone.api.home.queryDSL;

import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PriceRangeQueryDSL {
    List<Tuple> findStyleRankingWithPriceRanges(Integer minPrice, Integer maxPrice, Pageable pageable);
}
