package Designovel.Capstone.service.product;

import Designovel.Capstone.domain.*;
import Designovel.Capstone.entity.Category;
import Designovel.Capstone.entity.Product;
import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.entity.id.ProductId;
import Designovel.Capstone.repository.product.ProductRankingRepository;
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
import java.util.stream.Collectors;

import static Designovel.Capstone.entity.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.entity.QProductRanking.productRanking;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final ProductRankingRepository productRankingRepository;

    public Page<ProductRankingDTO> getProductRankingByFilter(ProductFilterDTO filter, int page) {
        int size = 20; // 페이지당 항목 수 고정
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = productRankingRepository.buildProductRankingFilter(filter);

        //노출 지수 가져오기
        QueryResults<Tuple> productRankingQueryResult = productRankingRepository.getExposureIndexFromProductRanking(builder, pageable);
        List<Tuple> exposureIndexQueryResult = productRankingQueryResult.getResults();
        long total = productRankingQueryResult.getTotal();

        //응답 객체 생성
        Map<String, ProductRankingDTO> productRankingMap = createProductRankingMap(exposureIndexQueryResult);

        List<ProductId> productIds = exposureIndexQueryResult.stream()
                .map(tuple -> tuple.get(categoryProduct.product).getId())
                .collect(Collectors.toList());

        //제일 최신 가격(현재가, 할인가) 가져오기
        List<Tuple> priceQueryResult = productRankingRepository.getPriceFromProductRanking(builder, productIds);
        updateProductPrices(productRankingMap, priceQueryResult);

        List<ProductRankingDTO> resultList = new ArrayList<>(productRankingMap.values());
        return new PageImpl<>(resultList, pageable, total);
    }

    private Map<String, ProductRankingDTO> createProductRankingMap(List<Tuple> rankScoreResult) {
        Map<String, ProductRankingDTO> resultMap = new HashMap<>();
        for (Tuple tuple : rankScoreResult) {
            Product product = tuple.get(categoryProduct.product);
            String brand = tuple.get(productRanking.brand);
            Float exposureIndex = tuple.get(productRanking.rankScore.sum());
            Category category = tuple.get(categoryProduct.category);
            String key = generateProductKey(product);
            if (resultMap.containsKey(key)) {
                DupeExposureIndex dupeExposureIndex = new DupeExposureIndex(product, exposureIndex, category);
                resultMap.get(key).getDupeExposureIndexList().add(dupeExposureIndex);
            } else {
                ProductRankingDTO productRankingDTO = new ProductRankingDTO(product, brand, exposureIndex);
                resultMap.put(key, productRankingDTO);
            }
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

    private String generateProductKey(Product product) {
        return product.getId().getProductId() + "_" + product.getId().getMallTypeId();
    }

    public List<String> getBrands(String mallType) {
        return productRankingRepository.findDistinctBrand(mallType);
    }


    public ProductBasicDetailDTO getExposureIndexAndPriceInfo(String productId, String mallType) {
        List<Object[]> rankScore = productRankingRepository.findRankScoreByProduct(productId, mallType);
        Pageable pageable = PageRequest.of(0, 1);
        ProductBasicDetailDTO productBasicDetailDTO = productRankingRepository.findPriceInfoByProduct(productId, mallType, pageable).getContent().get(0);

        List<DupeExposureIndex> exposureIndexList = rankScore.stream().map(data -> new DupeExposureIndex(productId, mallType, ((Number) data[1]).floatValue(), (Category) data[0]))
                .collect(Collectors.toList());
        productBasicDetailDTO.setExposureIndexList(exposureIndexList);
        return productBasicDetailDTO;
    }

    public List<TopBrandDTO> getTop10BrandsByMallType(ProductFilterDTO filter) {
        Pageable pageable = PageRequest.of(0, 10);
        BooleanBuilder builder = productRankingRepository.buildProductRankingFilter(filter);
        List<Tuple> top10BrandOrderByExposureIndex = productRankingRepository.getTop10BrandOrderByExposureIndex(builder, pageable);


        return top10BrandOrderByExposureIndex.stream()
                .map(tuple -> TopBrandDTO.builder()
                        .brand(tuple.get(productRanking.brand))
                        .exposureIndexSum(tuple.get(productRanking.rankScore.sum()))
                        .mallType(tuple.get(categoryProduct.product.id.mallTypeId.stringValue()))
                        .build())
                .collect(Collectors.toList());
    }

    public Map<String, List<ProductRanking>> getProductListByPriceRanges(String mallType) {
        List<Object[]> results = productRankingRepository.findAllProductsGroupedByPriceRange(mallType);
        Map<String, List<ProductRanking>> productsByPriceRange = new HashMap<>();

        for (Object[] result : results) {
            ProductRanking productRanking = (ProductRanking) result[0];
            String priceRange = (String) result[1];

            productsByPriceRange
                    .computeIfAbsent(priceRange, k -> new ArrayList<>())
                    .add(productRanking);
        }

        return productsByPriceRange;
    }
}
