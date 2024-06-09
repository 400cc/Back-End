package Designovel.Capstone.repository.querydsl.impl;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.QProductRanking;
import Designovel.Capstone.entity.id.ProductId;
import Designovel.Capstone.repository.querydsl.CustomProductRankingRepository;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

import static Designovel.Capstone.entity.QCategory.category;
import static Designovel.Capstone.entity.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.entity.QCategoryProduct.categoryProduct;
import static Designovel.Capstone.entity.QProductRanking.productRanking;


@Slf4j
@RequiredArgsConstructor
public class CustomProductRankingRepositoryImpl implements CustomProductRankingRepository {
    private final JPAQueryFactory jpaQueryFactory;


    private JPQLQuery<Date> createLatestCrawledDateSubQuery(QProductRanking subProductRanking) {
        BooleanBuilder subQueryConditions = new BooleanBuilder();
        subQueryConditions.and(subProductRanking.categoryProduct.id.eq(productRanking.categoryProduct.id));

        return JPAExpressions.select(subProductRanking.crawledDate.max())
                .from(subProductRanking)
                .where(subQueryConditions);
    }


    @Override
    public List<Tuple> getPriceFromProductRanking(BooleanBuilder builder, List<ProductId> productIdList) {
        QProductRanking subProductRanking = new QProductRanking("subProductRanking");
        JPQLQuery<Date> latestCrawledDateSubQuery = createLatestCrawledDateSubQuery(subProductRanking);

        return jpaQueryFactory.select(
                        categoryProduct.product,
                        productRanking.fixedPrice,
                        productRanking.discountedPrice,
                        productRanking.monetaryUnit,
                        categoryProduct.category
                )
                .from(productRanking)
                .leftJoin(productRanking.categoryProduct, categoryProduct)
                .where(builder.and(productRanking.crawledDate.eq(latestCrawledDateSubQuery))
                        .and(categoryProduct.product.id.in(productIdList)))
                .groupBy(categoryProduct.product.id.productId,
                        categoryProduct.product.id.mallTypeId,
                        categoryProduct.category)
                .fetch();
    }


    @Override
    public QueryResults<Tuple> getExposureIndexFromProductRanking(BooleanBuilder builder, Pageable pageable, String sortBy, String sortOrder) {
        OrderSpecifier<?> orderSpecifier = getProductFilterOrderSpecifier(sortBy, sortOrder);
        return jpaQueryFactory.select(
                        categoryProduct.product,
                        productRanking.brand,
                        productRanking.rankScore.sum(),
                        categoryProduct.category
                )
                .from(productRanking)
                .leftJoin(productRanking.categoryProduct, categoryProduct)
                .where(builder)
                .groupBy(categoryProduct.product.id.productId,
                        categoryProduct.product.id.mallTypeId,
                        categoryProduct.category.name)
                .orderBy(orderSpecifier)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetchResults();
    }

    private static OrderSpecifier<?> getProductFilterOrderSpecifier(String sortBy, String sortOrder) {
        Order order = sortOrder.equalsIgnoreCase("asc") ? Order.ASC : Order.DESC;
        OrderSpecifier<?> orderSpecifier;

        if ("exposureIndex".equals(sortBy)) {
            NumberExpression<Float> rankScoreSum = productRanking.rankScore.sum();
            orderSpecifier = new OrderSpecifier<>(order, rankScoreSum);
        } else {
            PathBuilder<?> entityPath = new PathBuilder<>(productRanking.getType(), "productRanking");

            switch (sortBy) {
                case "brand":
                    orderSpecifier = new OrderSpecifier<>(order, entityPath.getString(sortBy));
                    break;
                case "crawledDate":
                    orderSpecifier = new OrderSpecifier<>(order, entityPath.getDateTime(sortBy, Date.class));
                    break;
                // 필요한 필드에 대해 추가
                default:
                    throw new IllegalArgumentException("Invalid sortBy parameter");
            }
        }
        return orderSpecifier;
    }

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








