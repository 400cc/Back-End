package Designovel.Capstone.repository.querydsl;

import Designovel.Capstone.domain.ProductFilterDTO;
import com.querydsl.core.Tuple;

import java.util.List;

public interface CustomProductRankingRepository {
    List<Tuple> findAllWithFilters(ProductFilterDTO filter);
}
