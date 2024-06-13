package Designovel.Capstone.domain.product.productRanking;

import Designovel.Capstone.api.productFilter.dto.StyleBasicDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRankingRepository extends JpaRepository<ProductRanking, Integer> {

    @Query("select distinct p.brand from ProductRanking p where p.categoryProduct.product.id.mallTypeId = :mallTypeId")
    List<String> findDistinctBrand(@Param("mallTypeId") String mallTypeId);

    @Query("select p.categoryProduct.category, sum(p.rankScore) " +
            "from ProductRanking p " +
            "where p.categoryProduct.product.id.productId = :productId and p.categoryProduct.product.id.mallTypeId = :mallTypeId " +
            "group by p.categoryProduct.product, p.categoryProduct.category")
    List<Object[]> findRankScoreByProduct(@Param("productId") String productId, @Param("mallTypeId") String mallTypeId);

    @Query("select new Designovel.Capstone.api.productFilter.dto.StyleBasicDetailDTO(p.brand, p.discountedPrice, p.fixedPrice, p.monetaryUnit, p.crawledDate, p.categoryProduct.product.id) " +
            "from ProductRanking p " +
            "where p.categoryProduct.product.id.productId =:productId and p.categoryProduct.product.id.mallTypeId =:mallTypeId " +
            "order by p.crawledDate desc")
    Page<StyleBasicDetailDTO> findPriceInfoByProduct(@Param("productId") String productId, @Param("mallTypeId") String mallTypeId, Pageable pageable);

}
