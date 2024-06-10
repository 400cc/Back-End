package Designovel.Capstone.service.review;

import Designovel.Capstone.domain.HandsomeReviewDTO;
import Designovel.Capstone.domain.ReviewCountDTO;
import Designovel.Capstone.domain.ReviewFilterDTO;
import Designovel.Capstone.repository.review.HandsomeReviewRepository;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Designovel.Capstone.entity.QHandsomeReview.handsomeReview;

@Service
@Slf4j
@RequiredArgsConstructor
public class HandsomeReviewService {
    private final HandsomeReviewRepository handsomeReviewRepository;

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

}
