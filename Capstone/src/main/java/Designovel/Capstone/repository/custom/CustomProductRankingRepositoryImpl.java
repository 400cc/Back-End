package Designovel.Capstone.repository.custom;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.*;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class CustomProductRankingRepositoryImpl implements CustomProductRankingRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<ProductRanking> findAllWithFilters(ProductFilterDTO filter) {
        QProductRanking productRanking = QProductRanking.productRanking;
        QCategory category = QCategory.category;
        QCategoryClosure categoryClosure = QCategoryClosure.categoryClosure;
        QCategoryProduct categoryProduct = QCategoryProduct.categoryProduct;
        QImage image = QImage.image;
        QProduct product = QProduct.product;
        BooleanBuilder builder = new BooleanBuilder();

        if (filter.getBrand() != null && !filter.getBrand().isEmpty()) {
            builder.and(productRanking.brand.in(filter.getBrand()));
        }

        if (filter.getStartDate() != null) {
            builder.and(productRanking.crawledDate.goe(filter.getStartDate()));
        }

        if (filter.getEndDate() != null) {
            builder.and(productRanking.crawledDate.loe(filter.getEndDate()));
        }

        if (filter.getSite() != null) {
            builder.and(productRanking.product.id.mallType.eq(filter.getSite()));
        }

        if (filter.getCategory() != null && !filter.getCategory().isEmpty()) {
            builder.and(
                    JPAExpressions.selectOne()
                            .from(categoryProduct)
                            .join(categoryProduct.category, category)
                            .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                            .where(categoryClosure.id.ancestorId.in(filter.getCategory()) //
                                    .and(categoryProduct.category.eq(category))
                                    .and(categoryProduct.product.eq(productRanking.product)))
                            .exists()
            );

        }

        return jpaQueryFactory.selectFrom(productRanking)
                .where(builder)
                .fetch();

    }
}




