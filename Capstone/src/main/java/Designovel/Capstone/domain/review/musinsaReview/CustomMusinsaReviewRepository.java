package Designovel.Capstone.domain.review.musinsaReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import Designovel.Capstone.domain.review.handsomeReview.HandsomeReviewDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomMusinsaReviewRepository {
    List<Tuple> findMusinsaReviewCountsByFilter(ReviewFilterDTO filterDTO);

    Page<MusinsaReviewDTO> findMusinsaReviewPageByFilter(ReviewFilterDTO filterDTO);

    BooleanBuilder buildMusinsaReviewFilter(ReviewFilterDTO filterDTO);
}
