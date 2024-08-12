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

    public List<ReviewTrendDTO> getWConceptReviewTrend(String styleId) {
        List<Object[]> queryResult = wConceptReviewRepository.findReviewCountByStyleId(styleId);
        return reviewStyleService.processReviewTrendQueryResult(queryResult);
    }

    public Map<String, Object> getWConceptReviewPageByFilter(ReviewFilterDTO reviewFilterDTO) {
        ReviewCountDTO wconceptReviewCounts = getReviewCountDTOByFilter(reviewFilterDTO);
        Page<WConceptReviewDTO> wconceptReviewDTOPage = wConceptReviewRepository.findWConceptReviewPageByFilter(reviewFilterDTO);
        HashMap<String, Object> response = new HashMap<>();
        response.put("count", wconceptReviewCounts);
        response.put("review", wconceptReviewDTOPage);
        return response;
    }

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


}
