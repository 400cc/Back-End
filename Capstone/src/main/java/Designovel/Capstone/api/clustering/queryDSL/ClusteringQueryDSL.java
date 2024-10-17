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
    //클러스터링 결과에서 호버 시 상품 정보 조회 메서드
    ClusteringStyleDTO findStyleInfoById(String mallTypeId, String styleId);

    //클러스터링 결과에서 호버 시 상품에 맞는 카테고리 조회 메서드
    List<CategoryDTO> findCategoryListByStyle(String styleId);

    //가장 최신의 가격을 가져오기 위한 최근 날짜 조회 서브쿼리
    JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(String styleId);
}

