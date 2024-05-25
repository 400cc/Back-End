package Designovel.Capstone.service;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.repository.ProductRankingRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final ProductRankingRepository productRankingRepository;
    private final ImageService imageService;



    public Map<String, ProductRankingAvgDTO> getProductRankingAverages(ProductFilterDTO filter) {
        Map<String, ProductRankingAvgDTO> results = productRankingRepository.findAllWithFilters(filter);
        return results;
    }

//    private List<ProductRankingAvgDTO> processResults(List<Tuple> results) {
//        List<ProductRankingAvgDTO> averageDTOList = new ArrayList<>();
//
//        for (Tuple tuple : results) {
//            Product product = tuple.get(productRanking.product);
//            String brand = tuple.get(productRanking.brand);
//            Integer averageDiscountedPrice = tuple.get(productRanking.discountedPrice);
//            Integer averageFixedPrice = tuple.get(productRanking.fixedPrice);
//            Double exposureIndex = Double.valueOf(tuple.get(productRanking.rankScore.sum()));
//            String monetaryUnit = tuple.get(productRanking.monetaryUnit);
//            Category category = tuple.get(QCategory.category);
//            ProductRankingAvgDTO dto = new ProductRankingAvgDTO(
//                    product, brand, averageDiscountedPrice, averageFixedPrice, category, exposureIndex, monetaryUnit);
//
//            averageDTOList.add(dto);
//        }
//        imageService.setFirstSequenceImage(results);
//
//        return averageDTOList;
//    }


}
