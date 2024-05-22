package Designovel.Capstone.repository.querydsl;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.ProductRanking;

import java.util.List;

public interface CustomProductRankingRepository {
    List<ProductRanking> findAllWithFilters(ProductFilterDTO filter);
}
