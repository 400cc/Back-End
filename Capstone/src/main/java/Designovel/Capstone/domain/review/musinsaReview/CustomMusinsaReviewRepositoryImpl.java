package Designovel.Capstone.domain.review.musinsaReview;

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

import static Designovel.Capstone.domain.review.musinsaReview.QMusinsaReview.musinsaReview;

@Slf4j
@RequiredArgsConstructor
public class CustomMusinsaReviewRepositoryImpl implements CustomMusinsaReviewRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Tuple> findMusinsaReviewCountsByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildMusinsaReviewFilter(filterDTO);
        return jpaQueryFactory.select(
                        musinsaReview.rate,
                        musinsaReview.count())
                .from(musinsaReview)
                .where(builder)
                .groupBy(musinsaReview.rate)
                .fetch();
    }

    @Override
    public Page<MusinsaReviewDTO> findMusinsaReviewPageByFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = buildMusinsaReviewFilter(filterDTO);
        Pageable pageable = PageRequest.of(filterDTO.getPage(), 10);
        if (filterDTO.getRate() != null) {
            builder.and(musinsaReview.rate.in(filterDTO.getRate()));
        }
        List<MusinsaReviewDTO> MusinsaReviewDTOList =
                jpaQueryFactory.select(Projections.constructor(MusinsaReviewDTO.class,
                                musinsaReview.reviewId,
                                musinsaReview.styleId,
                                musinsaReview.orgReviewId,
                                musinsaReview.rate,
                                musinsaReview.writtenDate,
                                musinsaReview.body,
                                musinsaReview.likes,
                                musinsaReview.userInfo
                        ))
                        .from(musinsaReview)
                        .where(builder)
                        .orderBy(musinsaReview.writtenDate.desc())
                        .offset(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .fetch();

        long total = jpaQueryFactory
                .select(musinsaReview.count())
                .from(musinsaReview)
                .where(builder)
                .fetchOne();

        return new PageImpl<>(MusinsaReviewDTOList, pageable, total);
    }

    @Override
    public BooleanBuilder buildMusinsaReviewFilter(ReviewFilterDTO filterDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        String styleId = filterDTO.getStyleId();
        LocalDate startDate = filterDTO.getStartDate();

        builder.and(musinsaReview.styleId.eq(styleId));
        if (startDate != null) {
            builder.and(musinsaReview.writtenDate.goe(startDate));
        }
        return builder;
    }
}
