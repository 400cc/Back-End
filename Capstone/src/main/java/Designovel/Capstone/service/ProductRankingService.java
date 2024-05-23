package Designovel.Capstone.service;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.entity.Category;
import Designovel.Capstone.entity.CategoryProduct;
import Designovel.Capstone.entity.Image;
import Designovel.Capstone.entity.Product;
import Designovel.Capstone.repository.ProductRankingRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static Designovel.Capstone.entity.QProductRanking.productRanking;
import static Designovel.Capstone.entity.QProduct.product;

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
            Product product = tuple.get(productRanking.product);
            String brand = tuple.get(productRanking.brand);
            Double averageDiscountedPrice = tuple.get(productRanking.discountedPrice.avg());
            Double averageFixedPrice = tuple.get(productRanking.fixedPrice.avg());
            Double exposureIndex = Double.valueOf(tuple.get(productRanking.rankScore.sum()));
            String monetaryUnit = tuple.get(productRanking.monetaryUnit);
            ProductRankingAvgDTO dto = new ProductRankingAvgDTO(
                    product, brand, averageDiscountedPrice, averageFixedPrice, exposureIndex, monetaryUnit);

            averageDTOList.add(dto);
        }
        for (Tuple result : results) {
            Product product = result.get(productRanking.product);
            if (product != null) {
                product.setImages(
                        product.getImages().stream()
                                .filter(img -> img.getSequence() == 0)
                                .collect(Collectors.toList())
                );
            }
        }

        return averageDTOList;
    }
}
