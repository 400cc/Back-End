package Designovel.Capstone.domain.review.handsomeReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewCountDTO;
import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import Designovel.Capstone.api.styleFilter.dto.ReviewTrendDTO;
import Designovel.Capstone.domain.review.reviewProduct.ReviewStyleService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Designovel.Capstone.domain.review.handsomeReview.QHandsomeReview.handsomeReview;


@Service
@Slf4j
@RequiredArgsConstructor
public class HandsomeReviewService {
    private final HandsomeReviewRepository handsomeReviewRepository;
    private final ReviewStyleService reviewStyleService;


    /**
     * 한섬 리뷰 Pagination 반환 메서드
     * - 별점 당 리뷰 수 DB 조회
     * - 해당 상품의 리뷰 Pagination으로 조회
     * @param reviewFilterDTO
     * @return 별점 당 리뷰 수, Pagination 리뷰 결과 각각 반환
     */
    public Map<String, Object> getHandsomeReviewPageByFilter(ReviewFilterDTO reviewFilterDTO) {
        //별점 당 리뷰 수 조회
        ReviewCountDTO handsomeReviewCounts = getReviewCountDTOByFilter(reviewFilterDTO);

        //Pagination 리뷰 조회
        Page<HandsomeReviewDTO> handsomeReviewDTOPage = handsomeReviewRepository.findHandsomeReviewPageByFilter(reviewFilterDTO);

        HashMap<String, Object> response = new HashMap<>();
        response.put("count", handsomeReviewCounts);
        response.put("review", handsomeReviewDTOPage);
        return response;
    }

    /**
     * 한섬 리뷰 별점 당 리뷰 수 조회 및 처리 메서드
     * 1. 별점 당 리뷰 DB 조회
     * 2. 별점에 따라 리뷰 수 처리
     * @param reviewFilterDTO
     * @return ReviewCountDTO에 rate1 ... rate5 할당 후 반환
     */
    public ReviewCountDTO getReviewCountDTOByFilter(ReviewFilterDTO reviewFilterDTO) {
        List<Tuple> handsomeReviewCounts = handsomeReviewRepository.findHandsomeReviewCountsByFilter(reviewFilterDTO);
        Map<Integer, Integer> ratingCountMap = new HashMap<>();
        int total = 0;
        for (Tuple tuple : handsomeReviewCounts) {
            Integer rating = tuple.get(handsomeReview.rate);
            Long count = tuple.get(handsomeReview.count());
            int countValue = (count != null) ? count.intValue() : 0;
            ratingCountMap.put(rating, countValue);
            total += countValue;
        }
        return reviewStyleService.createReviewCountDTO(ratingCountMap, total);
    }

    /**
     * 한섬 리뷰 트랜드 처리 메서드
     * 1. 해당 상품 리뷰의 날짜 별 리뷰 수 조회
     * 2. 데이터 가공하여 반환
     * @param styleId
     * @return
     */
    public List<ReviewTrendDTO> getHandsomeReviewTrend(String styleId) {
        List<Object[]> queryResult = handsomeReviewRepository.findReviewCountByStyleId(styleId);
        return reviewStyleService.processReviewTrendQueryResult(queryResult);
    }

}
