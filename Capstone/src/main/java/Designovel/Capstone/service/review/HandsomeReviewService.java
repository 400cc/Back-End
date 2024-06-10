package Designovel.Capstone.service.review;

import Designovel.Capstone.domain.HandsomeReviewDTO;
import Designovel.Capstone.domain.ReviewCountDTO;
import Designovel.Capstone.repository.review.HandsomeReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class HandsomeReviewService {
    private final HandsomeReviewRepository handsomeReviewRepository;

    public ReviewCountDTO findReviewCountByProductId(String productId, Date startDate) {
        List<Object[]> reviewCountByProductId = handsomeReviewRepository.findReviewCountByProductIdAndWrittenDate(productId, startDate);
        Map<Integer, Integer> ratingCountMap = new HashMap<>();
        int total = 0;
        for (Object[] reviewCount : reviewCountByProductId) {
            int rating = (int) reviewCount[0];
            int count = ((Long) reviewCount[1]).intValue();
            ratingCountMap.put(rating, count);
            total += count;
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

    public Page<HandsomeReviewDTO> findReviewByProductIdAndRate(String productId, Date startDate, int page, int rate) {
        Pageable pageable = PageRequest.of(page, 10);
        return handsomeReviewRepository.findByProductIdAndRateAndWrittenDate(productId, startDate, rate, pageable);
    }
}
