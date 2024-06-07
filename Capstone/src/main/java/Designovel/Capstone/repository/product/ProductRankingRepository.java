package Designovel.Capstone.repository.product;

import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.domain.TopBrandDTO;
import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.repository.querydsl.CustomProductRankingRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRankingRepository extends JpaRepository<ProductRanking, Integer>, CustomProductRankingRepository {

    @Query("select distinct p.brand from ProductRanking p where p.categoryProduct.product.id.mallType = :mallType")
    List<String> findDistinctBrand(@Param("mallType") String mallType);

    @Query("select p.categoryProduct.category, sum(p.rankScore) " +
            "from ProductRanking p " +
            "where p.categoryProduct.product.id.productId = :productId and p.categoryProduct.product.id.mallType = :mallType " +
            "group by p.categoryProduct.product, p.categoryProduct.category")
    List<Object[]> findRankScoreByProduct(@Param("productId") String productId, @Param("mallType") String mallType);

    @Query("select new Designovel.Capstone.domain.ProductBasicDetailDTO(p.brand, p.discountedPrice, p.fixedPrice, p.monetaryUnit, p.crawledDate, p.categoryProduct.product.id) " +
            "from ProductRanking p " +
            "where p.categoryProduct.product.id.productId =:productId and p.categoryProduct.product.id.mallType =:mallType " +
            "order by p.crawledDate desc")
    Page<ProductBasicDetailDTO> findPriceInfoByProduct(@Param("productId") String productId, @Param("mallType") String mallType, Pageable pageable);

    @Query("select p, " +
            "case " +
            "    when p.fixedPrice < 10000 then '0-10k' " +
            "    when p.fixedPrice >= 10000 and p.fixedPrice < 20000 then '10k-20k' " +
            "    when p.fixedPrice >= 20000 and p.fixedPrice < 30000 then '20k-30k' " +
            "    when p.fixedPrice >= 30000 and p.fixedPrice < 40000 then '30k-40k' " +
            "    else '40k+' " +
            "end as priceRange " +
            "from ProductRanking p " +
            "where p.crawledDate in (" +
            "    select max(pr.crawledDate) " +
            "    from ProductRanking pr " +
            "    where pr.categoryProduct.id.productId = p.categoryProduct.id.productId " +
            "    and pr.categoryProduct.id.mallType = :mallType " +
            "    group by pr.categoryProduct.id" +
            ") " +
            "and p.categoryProduct.id.mallType = :mallType")
    List<Object[]> findAllProductsGroupedByPriceRange(@Param("mallType") String mallType);
}
