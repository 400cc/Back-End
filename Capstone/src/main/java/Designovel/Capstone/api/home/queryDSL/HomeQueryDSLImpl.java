package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.TopBrandFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
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

    @Override
    public BooleanBuilder buildTopBrandFilter(TopBrandFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterDTO.getStartDate() != null) {
            builder.and(productRanking.crawledDate.goe(filterDTO.getStartDate()));
        }

        if (filterDTO.getEndDate() != null) {
            builder.and(productRanking.crawledDate.loe(filterDTO.getEndDate()));
        }

        if (filterDTO.getMallTypeId() != null) {
            builder.and(productRanking.categoryProduct.id.mallTypeId.eq(filterDTO.getMallTypeId()));
        }

        if (filterDTO.getCategory() != null && !filterDTO.getCategory().isEmpty()) {
            // 카테고리 필터링 로직
            builder.and(
                    productRanking.categoryProduct.id.productId.in(
                            JPAExpressions.select(categoryProduct.id.productId)
                                    .from(categoryProduct)
                                    .join(categoryProduct.category, category)
                                    .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                                    .where(categoryClosure.id.ancestorId.in(filterDTO.getCategory()))
                    )
            );
        }

        return builder;
    }
}
