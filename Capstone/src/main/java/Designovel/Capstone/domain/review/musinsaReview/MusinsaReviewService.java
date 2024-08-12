package Designovel.Capstone.domain.review.musinsaReview;

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

import static Designovel.Capstone.domain.review.musinsaReview.QMusinsaReview.musinsaReview;

@Service
@RequiredArgsConstructor
public class MusinsaReviewService {
    private final MusinsaReviewRepository musinsaReviewRepository;
    private final ReviewStyleService reviewStyleService;

    public List<ReviewTrendDTO> getMusinsaReviewTrend(String styleId) {
        List<Object[]> queryResult = musinsaReviewRepository.findReviewCountByStyleId(styleId);
        return reviewStyleService.processReviewTrendQueryResult(queryResult);
    }

    public ReviewCountDTO getReviewCountDTOByFilter(ReviewFilterDTO reviewFilterDTO) {
        List<Tuple> musinsaReviewCounts = musinsaReviewRepository.findMusinsaReviewCountsByFilter(reviewFilterDTO);
        Map<Integer, Integer> ratingCountMap = new HashMap<>();
        int total = 0;
        for (Tuple tuple : musinsaReviewCounts) {
            Integer rating = tuple.get(musinsaReview.rate);
            Long count = tuple.get(musinsaReview.count());
            int countValue = (count != null) ? count.intValue() : 0;
            ratingCountMap.put(rating, countValue);
            total += countValue;
        }
        return reviewStyleService.createReviewCountDTO(ratingCountMap, total);
    }

    public Map<String, Object> getMusinsaReviewPageByFilter(ReviewFilterDTO reviewFilterDTO) {
        ReviewCountDTO musinsaReviewCounts = getReviewCountDTOByFilter(reviewFilterDTO);
        Page<MusinsaReviewDTO> musinsaReviewDTOPage = musinsaReviewRepository.findMusinsaReviewPageByFilter(reviewFilterDTO);
        HashMap<String, Object> response = new HashMap<>();
        response.put("count", musinsaReviewCounts);
        response.put("review", musinsaReviewDTOPage);
        return response;
    }
}
