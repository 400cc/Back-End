package Designovel.Capstone.api.home.queryDSL;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static Designovel.Capstone.domain.category.categoryProduct.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.domain.product.productRanking.QProductRanking.productRanking;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HomeQueryDSLImpl implements HomeQueryDSL {
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<Tuple> getTop10BrandOrderByExposureIndex(BooleanBuilder builder, Pageable pageable) {
        return jpaQueryFactory.select(
                        productRanking.brand,
                        productRanking.rankScore.sum(),
                        categoryProduct.product.id.mallTypeId
                )
                .from(productRanking)
                .leftJoin(productRanking.categoryProduct, categoryProduct)
                .where(builder)
                .groupBy(productRanking.brand,
                        categoryProduct.product.id.mallTypeId)
                .orderBy(productRanking.rankScore.sum().desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }
}
