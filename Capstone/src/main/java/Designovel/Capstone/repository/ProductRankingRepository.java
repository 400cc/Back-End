package Designovel.Capstone.repository;

import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.repository.querydsl.CustomProductRankingRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRankingRepository extends JpaRepository<ProductRanking, Integer>, CustomProductRankingRepository {

    @Query("select distinct p.brand from ProductRanking p where p.categoryProduct.product.id.mallType = :mallType")
    List<String> findDistinctBrand(@Param("mallType") String mallType);
}
