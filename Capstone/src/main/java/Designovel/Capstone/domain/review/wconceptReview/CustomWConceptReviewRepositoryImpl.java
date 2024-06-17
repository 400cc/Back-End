package Designovel.Capstone.domain.review.wconceptReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

import static Designovel.Capstone.domain.review.wconceptReview.QWConceptReview.wConceptReview;

@Slf4j
@RequiredArgsConstructor
public class CustomWConceptReviewRepositoryImpl implements CustomWConceptReviewRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> findWConceptReviewCountsByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildWConceptReviewFilter(filterDTO);
        return jpaQueryFactory.select(
                        wConceptReview.rate,
                        wConceptReview.count())
                .from(wConceptReview)
                .where(builder)
                .groupBy(wConceptReview.rate)
                .fetch();
    }

    @Override
    public Page<WConceptReviewDTO> findWConceptReviewPageByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildWConceptReviewFilter(filterDTO);
        Pageable pageable = PageRequest.of(filterDTO.getPage(), 10);
        if (filterDTO.getRate() != null) {
            builder.and(wConceptReview.rate.in(filterDTO.getRate()));
        }
        List<WConceptReviewDTO> wconceptReviewDTOList =
                jpaQueryFactory.select(Projections.constructor(WConceptReviewDTO.class,
                                wConceptReview.reviewId,
                                wConceptReview.styleId,
                                wConceptReview.orgReviewId,
                                wConceptReview.purchaseOption,
                                wConceptReview.sizeInfo,
                                wConceptReview.size,
                                wConceptReview.material,
                                wConceptReview.userId,
                                wConceptReview.writtenDate,
                                wConceptReview.body,
                                wConceptReview.rate,
                                wConceptReview.likes
                        ))
                        .from(wConceptReview)
                        .where(builder)
                        .orderBy(wConceptReview.writtenDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long total = jpaQueryFactory
                .select(wConceptReview.count())
                .from(wConceptReview)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(wconceptReviewDTOList, pageable, total);
    }

    @Override
    public BooleanBuilder buildWConceptReviewFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        String styleId = filterDTO.getStyleId();
        LocalDate startDate = filterDTO.getStartDate();

        builder.and(wConceptReview.styleId.eq(styleId));
        if (startDate != null) {
            builder.and(wConceptReview.writtenDate.goe(startDate));
        }
        return builder;
    }
}
