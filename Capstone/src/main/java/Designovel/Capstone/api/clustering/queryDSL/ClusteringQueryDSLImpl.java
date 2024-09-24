package Designovel.Capstone.api.clustering.queryDSL;

import Designovel.Capstone.api.clustering.dto.ClusterFilterDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringStyleDTO;
import Designovel.Capstone.domain.category.category.CategoryDTO;
import Designovel.Capstone.domain.style.styleRanking.QStyleRanking;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.*;
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
                        mallType
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

    @Override
    public void applyClusteringFilter(JPAQuery<ClusteringDTO> query, ClusterFilterDTO filterDTO) {

        if (filterDTO.getCategoryList() != null && !filterDTO.getCategoryList().isEmpty()) {
            query
                    .join(category).on(styleRanking.categoryId.eq(category.categoryId))
                    .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                    .where(categoryClosure.id.ancestorId.in(filterDTO.getCategoryList()));
        } else if (filterDTO.getMallTypeId() != null && !filterDTO.getMallTypeId().isEmpty()) {
            query.where(styleRanking.mallTypeId.eq(filterDTO.getMallTypeId()));
        }
    }

    @Override
    public ClusteringStyleDTO findStyleInfoById(String mallTypeId, String styleId) {
        JPQLQuery<LocalDate> latestCrawledDateSubQuery = createLatestCrawledDateSubQuery(styleId);
        return jpaQueryFactory
                .select(Projections.constructor(
                        ClusteringStyleDTO.class,
                        styleRanking.styleId,
                        styleRanking.styleName,
                        styleRanking.brand,
                        styleRanking.fixedPrice,
                        styleRanking.discountedPrice
                ))
                .from(styleRanking)
                .where(styleRanking.styleId.eq(styleId)
                        .and(styleRanking.mallTypeId.eq(mallTypeId))
                        .and(styleRanking.crawledDate.eq(latestCrawledDateSubQuery)))
                .fetchOne();
    }

    @Override
    public List<CategoryDTO> findCategoryListByStyle(String styleId) {
        return jpaQueryFactory
                .select(Projections.constructor(
                        CategoryDTO.class,
                        category
                ))
                .from(categoryStyle)
                .where(categoryStyle.style.id.styleId.eq(styleId))
                .fetch();
    }

    public JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(String styleId) {
        QStyleRanking subStyleRanking = new QStyleRanking("subStyleRanking");
        return JPAExpressions.select(subStyleRanking.crawledDate.max())
                .from(subStyleRanking)
                .where(subStyleRanking.styleId.eq(styleId));

    }

}
