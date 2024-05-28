package Designovel.Capstone.service;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.entity.Product;
import Designovel.Capstone.repository.ProductRankingRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
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

    public Page<ProductRankingAvgDTO> getProductRankingAverages(ProductFilterDTO filter, int page) {
        int size = 20; // 페이지당 항목 수 고정
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = productRankingRepository.buildProductRankingFilter(filter);

        //노출 지수 가져오기
        QueryResults<Tuple> productRankingQueryResult = productRankingRepository.getExposureIndexFromProductRanking(builder, pageable);
        List<Tuple> exposureIndexQueryResult = productRankingQueryResult.getResults();
        long total = productRankingQueryResult.getTotal();

        //응답 객체 생성
        Map<String, ProductRankingAvgDTO> productRankingMap = createProductRankingMap(exposureIndexQueryResult);

        //제일 최신 가격(현재가, 할인가) 가져오기
        List<Tuple> priceQueryResult = productRankingRepository.getPriceFromProductRanking(builder, filter.getEndDate(), pageable);
        updateProductPrices(productRankingMap, priceQueryResult);

        List<ProductRankingAvgDTO> resultList = new ArrayList<>(productRankingMap.values());
        return new PageImpl<>(resultList, pageable, total);
    }

    private Map<String, ProductRankingAvgDTO> createProductRankingMap(List<Tuple> rankScoreResult) {
        Map<String, ProductRankingAvgDTO> resultMap = new HashMap<>();
        for (Tuple tuple : rankScoreResult) {
            Product product = tuple.get(categoryProduct.product);
            String brand = tuple.get(productRanking.brand);
            Float exposureIndex = tuple.get(productRanking.rankScore.sum());

            String key = generateProductKey(product, tuple.get(categoryProduct.category.name));
            ProductRankingAvgDTO productRankingAvgDTO = new ProductRankingAvgDTO(product, brand, exposureIndex);
            resultMap.put(key, productRankingAvgDTO);
        }
        return resultMap;
    }

    private void updateProductPrices(Map<String, ProductRankingAvgDTO> resultMap, List<Tuple> priceResult) {
        for (Tuple tuple : priceResult) {
            Product product = tuple.get(categoryProduct.product);
            String key = generateProductKey(product, tuple.get(categoryProduct.category.name));
            ProductRankingAvgDTO productRankingAvgDTO = resultMap.get(key);
            if (productRankingAvgDTO != null) {
                productRankingAvgDTO.setDiscountedPrice(tuple.get(productRanking.discountedPrice));
                productRankingAvgDTO.setFixedPrice(tuple.get(productRanking.fixedPrice));
                productRankingAvgDTO.setMonetaryUnit(tuple.get(productRanking.monetaryUnit));
                productRankingAvgDTO.setCategory(tuple.get(categoryProduct.category));
            }
        }
    }

    private String generateProductKey(Product product, String categoryName) {
        return product.getId().getProductId() + "_" + product.getId().getMallType() + "_" + categoryName;
    }

    public List<String> getBrands(String mallType) {
        return productRankingRepository.findDistinctBrand(mallType);
    }
}
