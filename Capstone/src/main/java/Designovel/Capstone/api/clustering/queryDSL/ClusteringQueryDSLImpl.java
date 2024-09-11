package Designovel.Capstone.api.clustering.queryDSL;

import Designovel.Capstone.api.clustering.dto.ClusterFilterDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.domain.mallType.QMallType.*;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ClusteringQueryDSLImpl implements ClusteringQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Map<String, ClusteringDTO> findStyleInfoByCategory(ClusterFilterDTO filterDTO) {
        JPAQuery<ClusteringDTO> query = jpaQueryFactory
                .select(Projections.constructor(
                        ClusteringDTO.class,
                        styleRanking.styleId,
                        styleRanking.styleName,
                        mallType,
                        styleRanking.brand
                ))
                .from(styleRanking)
                .join(mallType)
                .on(styleRanking.mallTypeId.eq(mallType.mallTypeId))
                .distinct();

        applyClusteringFilter(query, filterDTO);
        List<ClusteringDTO> clusteringDTOList = query.fetch();

        return clusteringDTOList.stream()
                .collect(Collectors.toMap(
                        ClusteringDTO::getStyleId,
                        dto -> dto,
                        (existing, replacement) -> existing // 중복된 styleId가 있을 경우 첫 번째 항목 유지
                ));
    }

    private void applyClusteringFilter(JPAQuery<ClusteringDTO> query, ClusterFilterDTO filterDTO) {

        if (filterDTO.getCategoryList() != null && !filterDTO.getCategoryList().isEmpty()) {
            query
                    .join(category).on(styleRanking.categoryId.eq(category.categoryId))
                    .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                    .where(categoryClosure.id.ancestorId.in(filterDTO.getCategoryList()));
        } else if (filterDTO.getMallTypeId() != null && !filterDTO.getMallTypeId().isEmpty()) {
            query.where(styleRanking.mallTypeId.eq(filterDTO.getMallTypeId()));
        }
    }

}
