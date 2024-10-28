package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PriceRangeRepository {

    private final EntityManager entityManager;


    /**
     * 동적 필터 기반 가격대 별 상품 수 조회 메서드
     * @param filterDTO
     * @return
     */
    public List<Integer> findDiscountedPriceByFilter(HomeFilterDTO filterDTO) {
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT sr.discounted_price ")
                .append("FROM style_ranking sr ")
                .append("JOIN ( ")
                .append("    SELECT style_id, MAX(crawled_date) AS max_crawled_date ")
                .append("    FROM style_ranking ")
                .append("    WHERE 1=1 ");

        if (filterDTO.getEndDate() != null) {
            sql.append("AND crawled_date <= :endDate ");
        }

        sql.append("    GROUP BY style_id ")
                .append(") latest ON sr.style_id = latest.style_id AND sr.crawled_date = latest.max_crawled_date ");

        Map<String, Object> params = new HashMap<>();
        String filterSql = buildPriceRangeFilter(filterDTO, params);
        sql.append(filterSql);

        Query query = entityManager.createNativeQuery(sql.toString());
        params.forEach(query::setParameter);

        return query.getResultList();
    }



    /**
     * 가격대 별 상품 수 필터 생성 메서드
     * @param filterDTO
     * @param params
     * @return
     */
    public String buildPriceRangeFilter(HomeFilterDTO filterDTO, Map<String, Object> params) {
        StringBuilder filterSql = new StringBuilder();

        if (filterDTO.getMallTypeId() != null && !filterDTO.getMallTypeId().isEmpty()) {
            filterSql.append("AND sr.mall_type_id = :mallTypeId ");
            params.put("mallTypeId", filterDTO.getMallTypeId());
        }

        if (filterDTO.getStartDate() != null) {
            filterSql.append("AND sr.crawled_date >= :startDate ");
            params.put("startDate", filterDTO.getStartDate());
        }
        if (filterDTO.getEndDate() != null) {
            filterSql.append("AND sr.crawled_date <= :endDate ");
            params.put("endDate", filterDTO.getEndDate());
        }

        if (filterDTO.getCategory() != null && !filterDTO.getCategory().isEmpty()) {
            filterSql.append("AND sr.style_id IN ( ")
                    .append("    SELECT styleRanking.style_id ")
                    .append("    FROM style_ranking styleRanking ")
                    .append("    JOIN category c ON styleRanking.category_id = c.category_id ")
                    .append("    JOIN category_closure cc ON cc.descendant_id = c.category_id ")
                    .append("    WHERE cc.ancestor_id IN :categoryList ")
                    .append(") ");
            params.put("categoryList", filterDTO.getCategory());
        }

        return filterSql.toString();
    }
}

