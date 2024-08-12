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



    public Map<String, Object> getHandsomeReviewPageByFilter(ReviewFilterDTO reviewFilterDTO) {
        ReviewCountDTO handsomeReviewCounts = getReviewCountDTOByFilter(reviewFilterDTO);
        Page<HandsomeReviewDTO> handsomeReviewDTOPage = handsomeReviewRepository.findHandsomeReviewPageByFilter(reviewFilterDTO);
        HashMap<String, Object> response = new HashMap<>();
        response.put("count", handsomeReviewCounts);
        response.put("review", handsomeReviewDTOPage);
        return response;
    }

    public List<ReviewTrendDTO> getHandsomeReviewTrend(String styleId) {
        List<Object[]> queryResult = handsomeReviewRepository.findReviewCountByStyleId(styleId);
        return reviewStyleService.processReviewTrendQueryResult(queryResult);
    }

}
