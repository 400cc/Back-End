package Designovel.Capstone.api.productFilter.service;

import Designovel.Capstone.api.productFilter.dto.DupeExposureIndex;
import Designovel.Capstone.api.productFilter.dto.ProductFilterDTO;
import Designovel.Capstone.api.productFilter.queryDSL.ProductFilterQueryDSL;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.product.product.Product;
import Designovel.Capstone.domain.product.product.ProductId;
import Designovel.Capstone.domain.product.productRanking.ProductRankingDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.category.categoryProduct.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.domain.product.productRanking.QProductRanking.productRanking;

@Service
@RequiredArgsConstructor
public class ProductFilterService {

    private final ProductFilterQueryDSL productFilterQueryDSL;

    private static void addDuplicateExposureIndex(Map<String, ProductRankingDTO> resultMap, Product product, String brand, Float exposureIndex, Category category, String key) {
        if (resultMap.containsKey(key)) {
            DupeExposureIndex dupeExposureIndex = new DupeExposureIndex(product, exposureIndex, category);
            resultMap.get(key).getDupeExposureIndexList().add(dupeExposureIndex);
        } else {
            ProductRankingDTO productRankingDTO = new ProductRankingDTO(product, brand, exposureIndex);
            resultMap.put(key, productRankingDTO);
        }
    }

    public String generateProductKey(Product product) {
        return product.getId().getProductId() + "_" + product.getId().getMallTypeId();
    }

    public Page<ProductRankingDTO> getProductRankingByFilter(ProductFilterDTO filter, int page) {
        int size = 20; // 페이지당 항목 수 고정
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = productFilterQueryDSL.buildProductFilter(filter);

        //노출 지수 가져오기
        QueryResults<Tuple> productRankingQueryResult = productFilterQueryDSL.getExposureIndexInfo(builder, pageable, filter.getSortBy(), filter.getSortOrder());
        List<Tuple> exposureIndexQueryResult = productRankingQueryResult.getResults();
        long total = productRankingQueryResult.getTotal();

        //응답 객체 생성
        Map<String, ProductRankingDTO> productRankingMap = createProductRankingDTOMap(exposureIndexQueryResult);

        List<ProductId> productIds = exposureIndexQueryResult.stream()
                .map(tuple -> tuple.get(categoryProduct.product).getId())
                .collect(Collectors.toList());

        //제일 최신 가격(현재가, 할인가) 가져오기
        List<Tuple> priceQueryResult = productFilterQueryDSL.getPriceInfo(builder, productIds);
        updateProductPrices(productRankingMap, priceQueryResult);

        List<ProductRankingDTO> resultList = new ArrayList<>(productRankingMap.values());

        return new PageImpl<>(resultList, pageable, total);
    }

    private Map<String, ProductRankingDTO> createProductRankingDTOMap(List<Tuple> rankScoreResult) {
        Map<String, ProductRankingDTO> resultMap = new LinkedHashMap<>();
        for (Tuple tuple : rankScoreResult) {

            Product product = tuple.get(categoryProduct.product);
            String brand = tuple.get(productRanking.brand);
            Float exposureIndex = tuple.get(productRanking.rankScore.sum());
            Category category = tuple.get(categoryProduct.category);
            String key = generateProductKey(product);

            addDuplicateExposureIndex(resultMap, product, brand, exposureIndex, category, key);
        }
        return resultMap;
    }

    private void updateProductPrices(Map<String, ProductRankingDTO> resultMap, List<Tuple> priceResult) {
        for (Tuple tuple : priceResult) {
            Product product = tuple.get(categoryProduct.product);
            String key = generateProductKey(product);
            ProductRankingDTO productRankingDTO = resultMap.get(key);

            if (productRankingDTO != null && productRankingDTO.getDiscountedPrice() == null) {
                productRankingDTO.setDiscountedPrice(tuple.get(productRanking.discountedPrice));
                productRankingDTO.setFixedPrice(tuple.get(productRanking.fixedPrice));
                productRankingDTO.setMonetaryUnit(tuple.get(productRanking.monetaryUnit));
                productRankingDTO.setCategory(tuple.get(categoryProduct.category));

            }
        }
    }

}
