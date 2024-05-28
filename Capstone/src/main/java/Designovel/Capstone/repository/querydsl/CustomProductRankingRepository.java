package Designovel.Capstone.repository.querydsl;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomProductRankingRepository {

    BooleanBuilder buildProductRankingFilter(ProductFilterDTO filterDTO);
    QueryResults<Tuple> getExposureIndexFromProductRanking(BooleanBuilder builder, Pageable pageable);
    List<Tuple> getPriceFromProductRanking(BooleanBuilder builder, Date endDate, Pageable pageable);
}
