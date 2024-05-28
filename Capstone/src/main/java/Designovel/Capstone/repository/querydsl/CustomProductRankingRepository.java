package Designovel.Capstone.repository.querydsl;

import Designovel.Capstone.domain.ProductFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CustomProductRankingRepository {

    BooleanBuilder buildProductRankingFilter(ProductFilterDTO filterDTO);
    QueryResults<Tuple> getExposureIndexFromProductRanking(BooleanBuilder builder, Pageable pageable);
    List<Tuple> getPriceFromProductRanking(BooleanBuilder builder, Date endDate, Pageable pageable);
}
