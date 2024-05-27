package Designovel.Capstone.service;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.entity.Product;
import Designovel.Capstone.repository.ProductRankingRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Designovel.Capstone.entity.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.entity.QProductRanking.productRanking;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final ProductRankingRepository productRankingRepository;


    //    public Map<String, ProductRankingAvgDTO> getProductRankingAverages(ProductFilterDTO filter) {
//        Map<String, ProductRankingAvgDTO> results = productRankingRepository.findAllWithFilters(filter);
//        return results;
//    }
    public Page<ProductRankingAvgDTO> getProductRankingAverages(ProductFilterDTO filter, int page) {
        int size = 20; // 페이지당 항목 수 고정
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = productRankingRepository.buildProductRankingFilter(filter);

        List<Tuple> rankScoreResult = productRankingRepository.getExposureIndexFromProductRanking(builder, pageable);

        Map<String, ProductRankingAvgDTO> resultMap = new HashMap<>();
        for (Tuple tuple : rankScoreResult) {
            Product product = tuple.get(productRanking.product);
            String brand = tuple.get(productRanking.brand);
            Float exposureIndex = tuple.get(productRanking.rankScore.sum());

            String key = product.getId().getProductId() + "_" +
                    product.getId().getMallType() + "_" +
                    tuple.get(categoryProduct.category.name);
            ProductRankingAvgDTO productRankingAvgDTO = new ProductRankingAvgDTO(product, brand, exposureIndex);
            resultMap.put(key, productRankingAvgDTO);
        }

        List<Tuple> priceResult = productRankingRepository.getPriceFromProductRanking(builder, filter.getEndDate(), pageable);


        for (Tuple tuple : priceResult) {
            Product product = tuple.get(productRanking.product);
            String key = product.getId().getProductId() + "_" +
                    product.getId().getMallType() + "_" +
                    tuple.get(categoryProduct.category.name);
            ProductRankingAvgDTO productRankingAvgDTO = resultMap.get(key);
            if (productRankingAvgDTO != null) {
                productRankingAvgDTO.setDiscountedPrice(tuple.get(productRanking.discountedPrice));
                productRankingAvgDTO.setFixedPrice(tuple.get(productRanking.fixedPrice));
                productRankingAvgDTO.setMonetaryUnit(tuple.get(productRanking.monetaryUnit));
                productRankingAvgDTO.setCategory(tuple.get(categoryProduct.category));
            }
        }

        List<ProductRankingAvgDTO> resultList = new ArrayList<>(resultMap.values());

        return new PageImpl<>(resultList);

    }
}
