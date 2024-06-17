package Designovel.Capstone.domain.review.wconceptReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomWConceptReviewRepository {
    List<Tuple> findWConceptReviewCountsByFilter(ReviewFilterDTO filterDTO);

    Page<WConceptReviewDTO> findWConceptReviewPageByFilter(ReviewFilterDTO filterDTO);

    BooleanBuilder buildWConceptReviewFilter(ReviewFilterDTO filterDTO);
}
