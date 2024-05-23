package Designovel.Capstone.service;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.entity.CategoryProduct;
import Designovel.Capstone.entity.Image;
import Designovel.Capstone.repository.ProductRankingRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static Designovel.Capstone.entity.QProductRanking.productRanking;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final ProductRankingRepository productRankingRepository;

//    public List<ProductRanking> getProductRankings(ProductFilterDTO filter) {
//        return productRankingRepository.findAllWithFilters(filter);
//    }

    public List<ProductRankingAvgDTO> getProductRankingAverages(ProductFilterDTO filter) {
        List<Tuple> results = productRankingRepository.findAllWithFilters(filter);
        return processResults(results);
    }

    private List<ProductRankingAvgDTO> processResults(List<Tuple> results) {
        List<ProductRankingAvgDTO> averageDTOList = new ArrayList<>();

        for (Tuple tuple : results) {
            String productId = tuple.get(productRanking.product.id.productId);
            String brand = tuple.get(productRanking.brand);
            String mallType = tuple.get(productRanking.product.id.mallType);
            CategoryProduct category = (CategoryProduct) tuple.get(productRanking.product.categoryProducts);
            Double averageDiscountedPrice = tuple.get(productRanking.discountedPrice.avg());
            Double averageFixedPrice = tuple.get(productRanking.fixedPrice.avg());
            Double exposureIndex = Double.valueOf(tuple.get(productRanking.rankScore.sum()));
            String monetaryUnit = tuple.get(productRanking.monetaryUnit);
            Image images = (Image) tuple.get(productRanking.product.images);

            ProductRankingAvgDTO dto = new ProductRankingAvgDTO(
                    productId, brand, mallType, category, averageDiscountedPrice, averageFixedPrice, exposureIndex, monetaryUnit, images);

            averageDTOList.add(dto);
        }

        return averageDTOList;
    }
}
