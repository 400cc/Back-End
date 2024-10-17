package Designovel.Capstone.domain.review.wconceptReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewCountDTO;
import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import Designovel.Capstone.api.styleFilter.dto.ReviewTrendDTO;
import Designovel.Capstone.domain.review.reviewProduct.ReviewStyleService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Designovel.Capstone.domain.review.wconceptReview.QWConceptReview.wConceptReview;

@Service
@RequiredArgsConstructor
public class WConceptReviewService {
    private final WConceptReviewRepository wConceptReviewRepository;
    private final ReviewStyleService reviewStyleService;

    /**
     * W컨셉 리뷰 Pagination 반환 메서드
     * - 별점 당 리뷰 수 DB 조회
     * - 해당 상품의 리뷰 Pagination으로 조회
     * @param reviewFilterDTO
     * @return 별점 당 리뷰 수, Pagination 리뷰 결과 각각 반환
     */
    public Map<String, Object> getWConceptReviewPageByFilter(ReviewFilterDTO reviewFilterDTO) {
        ReviewCountDTO wconceptReviewCounts = getReviewCountDTOByFilter(reviewFilterDTO);
        Page<WConceptReviewDTO> wconceptReviewDTOPage = wConceptReviewRepository.findWConceptReviewPageByFilter(reviewFilterDTO);
        HashMap<String, Object> response = new HashMap<>();
        response.put("count", wconceptReviewCounts);
        response.put("review", wconceptReviewDTOPage);
        return response;
    }

    /**
     * W컨셉 리뷰 별점 당 리뷰 수 조회 및 처리 메서드
     * 1. 별점 당 리뷰 DB 조회
     * 2. 별점에 따라 리뷰 수 처리
     * @param reviewFilterDTO
     * @return ReviewCountDTO에 rate1 ... rate5 할당 후 반환
     */
    public ReviewCountDTO getReviewCountDTOByFilter(ReviewFilterDTO reviewFilterDTO) {
        List<Tuple> wconceptReviewCounts = wConceptReviewRepository.findWConceptReviewCountsByFilter(reviewFilterDTO);
        Map<Integer, Integer> ratingCountMap = new HashMap<>();
        int total = 0;
        for (Tuple tuple : wconceptReviewCounts) {
            Integer rating = tuple.get(wConceptReview.rate);
            Long count = tuple.get(wConceptReview.count());
            int countValue = (count != null) ? count.intValue() : 0;
            ratingCountMap.put(rating, countValue);
            total += countValue;
        }
        return reviewStyleService.createReviewCountDTO(ratingCountMap, total);
    }

    /**
     * W컨셉 리뷰 트랜드 처리 메서드
     * 1. 해당 상품 리뷰의 날짜 별 리뷰 수 조회
     * 2. 데이터 가공하여 반환
     * @param styleId
     * @return
     */
    public List<ReviewTrendDTO> getWConceptReviewTrend(String styleId) {
        List<Object[]> queryResult = wConceptReviewRepository.findReviewCountByStyleId(styleId);
        return reviewStyleService.processReviewTrendQueryResult(queryResult);
    }





}
