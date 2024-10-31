package Designovel.Capstone.api.clustering.queryDSL;

import Designovel.Capstone.api.clustering.dto.ClusteringStyleDTO;
import Designovel.Capstone.domain.category.category.CategoryDTO;
import Designovel.Capstone.domain.style.styleRanking.QStyleRanking;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.*;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ClusteringQueryDSLImpl implements ClusteringQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;


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
                .fetchFirst();
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

    @Override
    public JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(String styleId) {
        QStyleRanking subStyleRanking = new QStyleRanking("subStyleRanking");
        return JPAExpressions.select(subStyleRanking.crawledDate.max())
                .from(subStyleRanking)
                .where(subStyleRanking.styleId.eq(styleId));

    }

}
