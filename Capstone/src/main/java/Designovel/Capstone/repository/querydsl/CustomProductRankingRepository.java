package Designovel.Capstone.repository.querydsl;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.id.ProductId;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

public interface CustomProductRankingRepository {

    BooleanBuilder buildProductRankingFilter(ProductFilterDTO filterDTO);
    QueryResults<Tuple> getExposureIndexFromProductRanking(BooleanBuilder builder, Pageable pageable, String sortBy, String sortOrder);
    List<Tuple> getPriceFromProductRanking(BooleanBuilder builder, List<ProductId> productIdList);
    List<Tuple> getTop10BrandOrderByExposureIndex(BooleanBuilder builder, Pageable pageable);
//    List<Tuple> getProductRankingByPriceRange(BooleanBuilder builder, Pageable pageable);
    OrderSpecifier<?> getProductFilterOrderSpecifier(String sortBy, String sortOrder);

}
