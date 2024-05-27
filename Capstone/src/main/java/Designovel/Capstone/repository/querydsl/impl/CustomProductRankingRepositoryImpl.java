package Designovel.Capstone.repository.querydsl.impl;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.entity.Product;
import Designovel.Capstone.entity.QProductRanking;
import Designovel.Capstone.repository.querydsl.CustomProductRankingRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Designovel.Capstone.entity.QCategory.category;
import static Designovel.Capstone.entity.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.entity.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.entity.QProduct.product;
import static Designovel.Capstone.entity.QProductRanking.productRanking;


@Slf4j
@RequiredArgsConstructor
public class CustomProductRankingRepositoryImpl implements CustomProductRankingRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> getPriceFromProductRanking(BooleanBuilder builder, Date endDate, Pageable pageable) {
        QProductRanking subProductRanking = new QProductRanking("subProductRanking");

        BooleanBuilder subQueryConditions = new BooleanBuilder();
        subQueryConditions.and(subProductRanking.product.id.productId.eq(productRanking.product.id.productId))
                .and(subProductRanking.product.id.mallType.eq(productRanking.product.id.mallType))
                .and(subProductRanking.product.categoryProducts.any().category.eq(categoryProduct.category));

        if (endDate != null) {
            subQueryConditions.and(subProductRanking.crawledDate.loe(endDate));
        }
        JPQLQuery<Date> latestCrawledDateSubQuery = JPAExpressions
                .select(subProductRanking.crawledDate.max())
                .from(subProductRanking)
                .where(subQueryConditions);
        return jpaQueryFactory.select(
                        productRanking.product,
                        productRanking.fixedPrice,
                        productRanking.discountedPrice,
                        productRanking.monetaryUnit,
                        categoryProduct.category.name,
                        categoryProduct.category
                )
                .from(productRanking)
                .leftJoin(productRanking.product, product)
                .leftJoin(product.categoryProducts, categoryProduct)
                .where(builder.and(productRanking.crawledDate.eq(latestCrawledDateSubQuery)))
                .groupBy(productRanking.product.id.productId,
                        productRanking.product.id.mallType,
                        categoryProduct.category.name,
                        productRanking.discountedPrice,
                        productRanking.fixedPrice)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }


    @Override
    public BooleanBuilder buildProductRankingFilter(ProductFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();

        if (filterDTO.getBrand() != null) {
            builder.and(productRanking.brand.in(filterDTO.getBrand()));
        }

        if (filterDTO.getStartDate() != null) {
            builder.and(productRanking.crawledDate.goe(filterDTO.getStartDate()));
        }

        if (filterDTO.getEndDate() != null) {
            builder.and(productRanking.crawledDate.loe(filterDTO.getEndDate()));
        }

        if (filterDTO.getMallType() != null) {
            builder.and(productRanking.product.id.mallType.eq(filterDTO.getMallType()));
        }

        if (filterDTO.getCategory() != null && !filterDTO.getCategory().isEmpty()) {
            // 카테고리 필터링 로직
            builder.and(
                    productRanking.product.id.productId.in(
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

    @Override
    public List<Tuple> getExposureIndexFromProductRanking(BooleanBuilder builder, Pageable pageable) {
        return jpaQueryFactory.select(
                        productRanking.product,
                        productRanking.brand,
                        productRanking.rankScore.sum(),
                        categoryProduct.category.name
                )
                .from(productRanking)
                .leftJoin(productRanking.product, product)
                .leftJoin(product.categoryProducts, categoryProduct)
                .where(builder)
                .groupBy(productRanking.product.id.productId,
                        productRanking.product.id.mallType,
                        categoryProduct.category.name)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

}








