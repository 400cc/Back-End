package Designovel.Capstone.domain.product.productRanking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final ProductRankingRepository productRankingRepository;

    public List<String> getBrandsByMallTypeId(String mallTypeId) {
        return productRankingRepository.findDistinctBrand(mallTypeId);
    }

}
