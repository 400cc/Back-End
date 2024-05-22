package Designovel.Capstone.repository;

import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.repository.querydsl.CustomProductRankingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRankingRepository extends JpaRepository<ProductRanking, Integer>, CustomProductRankingRepository {
}
