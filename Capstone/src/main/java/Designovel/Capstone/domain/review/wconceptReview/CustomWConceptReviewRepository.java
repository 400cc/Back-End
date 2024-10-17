package Designovel.Capstone.domain.review.wconceptReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomWConceptReviewRepository {
    //W컨셉 상품 리뷰 별점 별로 리뷰 수 반환 메서드
    List<Tuple> findWConceptReviewCountsByFilter(ReviewFilterDTO filterDTO);

    //W컨셉 상품 리뷰 Pagination으로 반환 메서드
    Page<WConceptReviewDTO> findWConceptReviewPageByFilter(ReviewFilterDTO filterDTO);

    //W컨셉 상품 리뷰 동적 필터 구현 메서드
    BooleanBuilder buildWConceptReviewFilter(ReviewFilterDTO filterDTO);
}
