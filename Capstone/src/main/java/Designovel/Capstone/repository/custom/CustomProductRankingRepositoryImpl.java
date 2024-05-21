package Designovel.Capstone.repository.custom;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.entity.QImage;
import Designovel.Capstone.entity.QProduct;
import Designovel.Capstone.entity.QProductRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;


@RequiredArgsConstructor
public class CustomProductRankingRepositoryImpl implements CustomProductRankingRepository {
    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<ProductRanking> findAllWithFilters(ProductFilterDTO filter) {
        QProductRanking productRanking = QProductRanking.productRanking;
        QProduct product = QProduct.product; // 변경된 필드 이름 반영
        QImage image = QImage.image;
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

        return jpaQueryFactory.selectFrom(productRanking)
                .where(builder)
                .join(product.images, image)
                .fetch();

    }
}




