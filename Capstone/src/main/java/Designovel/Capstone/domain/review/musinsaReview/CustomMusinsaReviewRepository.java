package Designovel.Capstone.domain.review.musinsaReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import Designovel.Capstone.domain.review.handsomeReview.HandsomeReviewDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomMusinsaReviewRepository {
    //무신사 상품 리뷰 별점 별로 리뷰 수 반환 메서드
    List<Tuple> findMusinsaReviewCountsByFilter(ReviewFilterDTO filterDTO);

    //무신사 상품 리뷰 Pagination으로 반환 메서드
    Page<MusinsaReviewDTO> findMusinsaReviewPageByFilter(ReviewFilterDTO filterDTO);

    //무산사 상품 리뷰 동적 필터 구현 메서드
    BooleanBuilder buildMusinsaReviewFilter(ReviewFilterDTO filterDTO);
}
