package Designovel.Capstone.repository.querydsl.impl;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.*;
import Designovel.Capstone.repository.querydsl.CustomProductRankingRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


import static Designovel.Capstone.entity.QImage.image;



@RequiredArgsConstructor
public class CustomProductRankingRepositoryImpl implements CustomProductRankingRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Tuple> findAllWithFilters(ProductFilterDTO filter) {
        QProductRanking productRanking = QProductRanking.productRanking;
        QCategory category = QCategory.category;
        QCategoryClosure categoryClosure = QCategoryClosure.categoryClosure;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getBrand() != null) {
            builder.and(productRanking.brand.in(filter.getBrand()));
        }

        if (filter.getStartDate() != null) {
            builder.and(productRanking.crawledDate.goe(filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            builder.and(productRanking.crawledDate.loe(filter.getEndDate()));
        }

        if (filter.getMallType() != null) {
            builder.and(productRanking.product.id.mallType.eq(filter.getMallType()));
        }

        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            // 카테고리 필터링 로직
            builder.and(
                    productRanking.product.id.productId.in(
                            JPAExpressions.select(categoryProduct.id.productId)
                                    .from(categoryProduct)
                                    .join(categoryProduct.category, category)
                                    .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                                    .where(categoryClosure.id.ancestorId.in(filter.getCategory()))
                    )
            );
        }
//
//        return jpaQueryFactory.selectFrom(productRanking)
//                .where(builder)
//                .fetch();

        List<Tuple> results = jpaQueryFactory.select(
                        productRanking.product.id.productId,
                        productRanking.brand,
                        productRanking.product.id.mallType,
                        productRanking.discountedPrice.avg(),
                        productRanking.fixedPrice.avg(),
                        productRanking.rankScore.sum(),
                        productRanking.monetaryUnit,
                        productRanking.product.categoryProducts,
                        image
                )
                .from(productRanking)
                .where(builder)
                .leftJoin(productRanking.product.images, image)
                .groupBy(productRanking.product.id.productId,
                        productRanking.brand,
                        productRanking.product.id.mallType,
                        productRanking.monetaryUnit)
                .fetch();

        return results;
    }

}




