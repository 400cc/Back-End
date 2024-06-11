package Designovel.Capstone.api.home.service;

import Designovel.Capstone.api.home.dto.TopBrandDTO;
import Designovel.Capstone.api.home.queryDSL.HomeQueryDSL;
import Designovel.Capstone.api.productFilter.dto.ProductFilterDTO;
import Designovel.Capstone.api.productFilter.queryDSL.ProductFilterQueryDSL;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.category.categoryProduct.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.domain.product.productRanking.QProductRanking.productRanking;

@Service
@RequiredArgsConstructor
public class TopBrandService {

    private final ProductFilterQueryDSL productFilterQueryDSL;
    private final HomeQueryDSL homeQueryDSL;

    public List<TopBrandDTO> getTop10BrandsByMallType(ProductFilterDTO filter) {
        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder builder = productFilterQueryDSL.buildProductRankingFilter(filter);
        List<Tuple> top10BrandOrderByExposureIndex = homeQueryDSL.getTop10BrandOrderByExposureIndex(builder, pageable);

        return top10BrandOrderByExposureIndex.stream()
                .map(tuple -> TopBrandDTO.builder()
                        .brand(tuple.get(productRanking.brand))
                        .exposureIndexSum(tuple.get(productRanking.rankScore.sum()))
                        .mallType(tuple.get(categoryProduct.product.id.mallTypeId.stringValue()))
                        .build())
                .collect(Collectors.toList());
    }
}
