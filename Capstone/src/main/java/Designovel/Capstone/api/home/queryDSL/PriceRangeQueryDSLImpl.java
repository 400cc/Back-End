package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import com.querydsl.jpa.impl.JPAQueryFactory;
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
public class PriceRangeQueryDSLImpl implements PriceRangeQueryDSL {

    private final EntityManager entityManager;

    @Override
    public List<Integer> findDiscountedPriceByFilter(HomeFilterDTO filterDTO) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT sr.discounted_price ")
                .append("FROM style_ranking sr ")
                .append("JOIN ( ")
                .append("    SELECT MAX(sub_sr.crawled_date) AS latest_crawled_date, sub_sr.style_id ")
                .append("    FROM style_ranking sub_sr ")
                .append("    WHERE 1=1 ");


        // endDate 조건을 동적으로 추가
        if (filterDTO.getEndDate() != null) {
            sql.append("AND sub_sr.crawled_date <= :endDate ");
        }

        sql.append("    GROUP BY sub_sr.style_id, sub_sr.mall_type_id ")
                .append(") latest ON sr.style_id = latest.style_id AND sr.crawled_date = latest.latest_crawled_date ")
                .append("WHERE 1=1 ");


        Map<String, Object> params = new HashMap<>();
        String filterSql = buildPriceRangeFilter(filterDTO, params);
        sql.append(filterSql);

        Query query = entityManager.createNativeQuery(sql.toString());
        params.forEach(query::setParameter);

        return query.getResultList();
    }


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

