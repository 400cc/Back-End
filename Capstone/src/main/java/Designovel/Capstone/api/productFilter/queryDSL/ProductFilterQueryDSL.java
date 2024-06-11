package Designovel.Capstone.api.productFilter.queryDSL;

import Designovel.Capstone.api.productFilter.dto.ProductFilterDTO;
import Designovel.Capstone.domain.product.product.ProductId;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductFilterQueryDSL {
    QueryResults<Tuple> getExposureIndexInfo(BooleanBuilder builder, Pageable pageable, String sortBy, String sortOrder);
    List<Tuple> getPriceInfo(BooleanBuilder builder, List<ProductId> productIdList);
    OrderSpecifier<?> getProductFilterOrderSpecifier(String sortBy, String sortOrder);
    BooleanBuilder buildProductFilter(ProductFilterDTO filterDTO);

}
