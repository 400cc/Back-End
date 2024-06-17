package Designovel.Capstone.domain.style.styleRanking;

import Designovel.Capstone.api.styleFilter.dto.StyleBasicDetailDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StyleRankingRepository extends JpaRepository<StyleRanking, Integer> {

    @Query("select distinct p.brand from StyleRanking p where p.categoryStyle.style.id.mallTypeId = :mallTypeId")
    List<String> findDistinctBrand(@Param("mallTypeId") String mallTypeId);

    @Query("select p.categoryStyle.category, sum(p.rankScore) " +
            "from StyleRanking p " +
            "where p.categoryStyle.style.id.styleId = :styleId and p.categoryStyle.style.id.mallTypeId = :mallTypeId " +
            "group by p.categoryStyle.style, p.categoryStyle.category")
    List<Object[]> findRankScoreByStyle(@Param("styleId") String styleId, @Param("mallTypeId") String mallTypeId);

    @Query("select new Designovel.Capstone.api.styleFilter.dto.StyleBasicDetailDTO(p.brand, p.discountedPrice, p.fixedPrice, p.monetaryUnit, p.crawledDate, p.categoryStyle.style.id, p.styleName) " +
            "from StyleRanking p " +
            "where p.categoryStyle.style.id.styleId =:styleId and p.categoryStyle.style.id.mallTypeId =:mallTypeId " +
            "order by p.crawledDate desc")
    Page<StyleBasicDetailDTO> findPriceInfoByStyle(@Param("styleId") String styleId, @Param("mallTypeId") String mallTypeId, Pageable pageable);

    @Query("select min(p.fixedPrice), max(p.fixedPrice) from StyleRanking p " +
            "where p.categoryStyle.id.mallTypeId = :mallTypeId")
    Object[] findMinMaxFixedPriceByMallTypeId(@Param("mallTypeId") String mallTypeId);
}
