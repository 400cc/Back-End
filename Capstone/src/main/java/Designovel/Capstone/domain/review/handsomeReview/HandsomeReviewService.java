package Designovel.Capstone.domain.review.handsomeReview;

import Designovel.Capstone.api.productFilter.dto.ReviewCountDTO;
import Designovel.Capstone.api.productFilter.dto.ReviewFilterDTO;
import Designovel.Capstone.domain.review.reviewProduct.ReviewProductService;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Designovel.Capstone.domain.review.handsomeReview.QHandsomeReview.handsomeReview;


@Service
@Slf4j
@RequiredArgsConstructor
public class HandsomeReviewService {
    private final HandsomeReviewRepository handsomeReviewRepository;
    private final ReviewProductService reviewProductService;

    public ReviewCountDTO getReviewCountDTOByFilter(ReviewFilterDTO reviewFilterDTO) {
        List<Tuple> handsomeReviewCounts = handsomeReviewRepository.findHandsomeReviewCountsByFilter(reviewFilterDTO);
        Map<Integer, Integer> ratingCountMap = new HashMap<>();
        int total = 0;
        for (Tuple tuple : handsomeReviewCounts) {
            Integer rating = tuple.get(handsomeReview.rating);
            Long count = tuple.get(handsomeReview.count());

            int countValue = (count != null) ? count.intValue() : 0;
            ratingCountMap.put(rating, countValue);
            total += countValue;
        }
        ReviewCountDTO reviewCountDTO = new ReviewCountDTO();
        reviewCountDTO.setRate1(ratingCountMap.getOrDefault(1, 0));
        reviewCountDTO.setRate2(ratingCountMap.getOrDefault(2, 0));
        reviewCountDTO.setRate3(ratingCountMap.getOrDefault(3, 0));
        reviewCountDTO.setRate4(ratingCountMap.getOrDefault(4, 0));
        reviewCountDTO.setRate5(ratingCountMap.getOrDefault(5, 0));
        reviewCountDTO.setTotal(total);
        return reviewCountDTO;
    }


    public Map<String, Object> getHandsomeReviewPageByFilter(ReviewFilterDTO reviewFilterDTO) {
        ReviewCountDTO reviewCountByProductId = getReviewCountDTOByFilter(reviewFilterDTO);
        Page<HandsomeReviewDTO> handsomeReviewDTOPage = handsomeReviewRepository.findHandsomeReviewPageByFilter(reviewFilterDTO);
        HashMap<String, Object> response = new HashMap<>();
        response.put("count", reviewCountByProductId);
        response.put("review", handsomeReviewDTOPage);
        return response;
    }

    public Map<LocalDate, Integer> getReviewTrend(String productId) {
        List<Object[]> queryResult = handsomeReviewRepository.findReviewCountByProductIdAndDate(productId);
        LocalDate startDate = queryResult.isEmpty() ? null : (LocalDate) queryResult.get(0)[0];
        LocalDate endDate = queryResult.isEmpty() ? null : (LocalDate) queryResult.get(queryResult.size() - 1)[0];
        log.info(String.valueOf(startDate));
        log.info(String.valueOf(endDate));
        Map<LocalDate, Integer> dateRangeMap = reviewProductService.createDateRangeMap(startDate, endDate);
        for (Object[] object : queryResult) {
            LocalDate crawledDate = (LocalDate) object[0];
            Long longReviewCount = (Long) object[1];
            Integer reviewCount = (longReviewCount != null) ? longReviewCount.intValue() : 0;
            dateRangeMap.put(crawledDate, reviewCount);
        }
        return dateRangeMap;
    }

}
