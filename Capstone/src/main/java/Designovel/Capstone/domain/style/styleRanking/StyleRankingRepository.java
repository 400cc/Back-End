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

    /**
     * 쇼핑몰의 중복 되지 않은 브랜드 DB 조회 메서드
     * @param mallTypeId
     * @return
     */
    @Query("select distinct brand " +
            "from StyleRanking " +
            "where mallTypeId = :mallTypeId " +
            "order by brand asc")
    List<String> findDistinctBrand(@Param("mallTypeId") String mallTypeId);

    /**
     * 상품의 카테고리, 노출지수 DB 조회 메서드
     * @param styleId
     * @param mallTypeId
     * @return
     */
    @Query("select p.categoryStyle.category, sum(p.rankScore) " +
            "from StyleRanking p " +
            "where p.categoryStyle.style.id.styleId = :styleId and p.categoryStyle.style.id.mallTypeId = :mallTypeId " +
            "group by p.categoryStyle.style, p.categoryStyle.category")
    List<Object[]> findRankScoreByStyle(@Param("styleId") String styleId, @Param("mallTypeId") String mallTypeId);

    /**
     * 상품의 가격 및 기타 공통 정보 조회 메서드
     * @param styleId
     * @param mallTypeId
     * @param pageable
     * @return
     */
    @Query("select new Designovel.Capstone.api.styleFilter.dto.StyleBasicDetailDTO(p.brand, p.discountedPrice, p.fixedPrice, p.monetaryUnit, p.crawledDate, p.styleId, p.mallTypeId, p.styleName) " +
            "from StyleRanking p " +
            "where p.styleId =:styleId and p.mallTypeId =:mallTypeId " +
            "order by p.crawledDate desc")
    Page<StyleBasicDetailDTO> findPriceInfoByStyle(@Param("styleId") String styleId, @Param("mallTypeId") String mallTypeId, Pageable pageable);
}
