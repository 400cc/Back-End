package Designovel.Capstone.domain.review.handsomeReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomHandsomeReviewRepository {
    //한섬 상품 리뷰 별점 별로 리뷰 수 반환 메서드
    List<Tuple> findHandsomeReviewCountsByFilter(ReviewFilterDTO filterDTO);

    //한섬 상품 리뷰 Pagination으로 반환 메서드
    Page<HandsomeReviewDTO> findHandsomeReviewPageByFilter(ReviewFilterDTO filterDTO);

    //한섬 상품 리뷰 동적 필터 구현 메서드
    BooleanBuilder buildHandsomeReviewFilter(ReviewFilterDTO filterDTO);
}
