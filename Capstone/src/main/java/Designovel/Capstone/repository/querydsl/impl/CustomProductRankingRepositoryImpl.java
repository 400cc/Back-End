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
    public Map<String, ProductRankingAvgDTO> findAllWithFilters(ProductFilterDTO filter) {
        BooleanBuilder builder = buildProductRankingFilter(filter);

        List<Tuple> rankScoreResult = getExposureIndexFromProductRanking(builder);

        Map<String, ProductRankingAvgDTO> resultMap = new HashMap<>();
        for (Tuple tuple : rankScoreResult) {
            Product product = tuple.get(productRanking.product);
            String brand = tuple.get(productRanking.brand);
            Float exposureIndex = tuple.get(productRanking.rankScore.sum());

            String key = product.getId().getProductId() + "_" +
                    product.getId().getMallType() + "_" +
                    tuple.get(categoryProduct.category.name);
            ProductRankingAvgDTO productRankingAvgDTO = new ProductRankingAvgDTO(product, brand, exposureIndex);
            resultMap.put(key, productRankingAvgDTO);
        }

        List<Tuple> priceResult = getPriceFromProductRanking(builder, filter.getEndDate());


        for (Tuple tuple : priceResult) {
            Product product = tuple.get(productRanking.product);
            String key = product.getId().getProductId() + "_" +
                    product.getId().getMallType() + "_" +
                    tuple.get(categoryProduct.category.name);
            resultMap.get(key).setDiscountedPrice(tuple.get(productRanking.discountedPrice));
            resultMap.get(key).setFixedPrice(tuple.get(productRanking.fixedPrice));
            resultMap.get(key).setMonetaryUnit(tuple.get(productRanking.monetaryUnit));
            resultMap.get(key).setCategory(tuple.get(categoryProduct.category));
        }

        return resultMap;
    }

    @Override
    public List<Tuple> getPriceFromProductRanking(BooleanBuilder builder, Date endDate) {
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
    public List<Tuple> getExposureIndexFromProductRanking(BooleanBuilder builder) {
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
                .fetch();
    }

}








