package Designovel.Capstone.api.clustering.queryDSL;

import Designovel.Capstone.api.clustering.dto.ClusterFilterDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringStyleDTO;
import Designovel.Capstone.domain.category.category.CategoryDTO;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public interface ClusteringQueryDSL {
    ClusteringStyleDTO findStyleInfoById(String mallTypeId, String styleId);

    List<CategoryDTO> findCategoryListByStyle(String styleId);

    JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(String styleId);
}

