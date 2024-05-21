package Designovel.Capstone.service;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.repository.custom.CustomProductRankingRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductRankingService {

    private final CustomProductRankingRepositoryImpl customProductRankingRepository;

    public List<ProductRanking> getProductRankings(ProductFilterDTO filter) {
        return customProductRankingRepository.findAllWithFilters(filter);
    }

}
