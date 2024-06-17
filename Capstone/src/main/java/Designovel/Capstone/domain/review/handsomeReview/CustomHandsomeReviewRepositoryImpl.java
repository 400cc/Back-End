package Designovel.Capstone.domain.review.handsomeReview;

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

import static Designovel.Capstone.domain.review.handsomeReview.QHandsomeReview.handsomeReview;


@Slf4j
@RequiredArgsConstructor
public class CustomHandsomeReviewRepositoryImpl implements CustomHandsomeReviewRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public BooleanBuilder buildHandsomeReviewFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        String styleId = filterDTO.getStyleId();
        LocalDate startDate = filterDTO.getStartDate();

        builder.and(handsomeReview.styleId.eq(styleId));
        if (startDate != null) {
            builder.and(handsomeReview.writtenDate.goe(startDate));
        }
        return builder;
    }

    @Override
    public List<Tuple> findHandsomeReviewCountsByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildHandsomeReviewFilter(filterDTO);
        return jpaQueryFactory.select(
                        handsomeReview.rate,
                        handsomeReview.count())
                .from(handsomeReview)
                .where(builder)
                .groupBy(handsomeReview.rate)
                .fetch();
    }

    @Override
    public Page<HandsomeReviewDTO> findHandsomeReviewPageByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildHandsomeReviewFilter(filterDTO);
        Pageable pageable = PageRequest.of( filterDTO.getPage(), 10);
        if (filterDTO.getRate() != null) {
            builder.and(handsomeReview.rate.in(filterDTO.getRate()));
        }
        List<HandsomeReviewDTO> handsomeReviewDTOList =
                jpaQueryFactory.select(Projections.constructor(HandsomeReviewDTO.class,
                                handsomeReview.reviewId,
                                handsomeReview.styleId,
                                handsomeReview.orgReviewId,
                                handsomeReview.rate,
                                handsomeReview.writtenDate,
                                handsomeReview.userId,
                                handsomeReview.body,
                                handsomeReview.styleColor,
                                handsomeReview.styleSize,
                                handsomeReview.importSource,
                                handsomeReview.userHeight,
                                handsomeReview.userSize))
                        .from(handsomeReview)
                        .where(builder)
                        .orderBy(handsomeReview.writtenDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long total = jpaQueryFactory
                .select(handsomeReview.count())
                .from(handsomeReview)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(handsomeReviewDTOList, pageable, total);
    }
}
