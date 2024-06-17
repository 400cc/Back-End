package Designovel.Capstone.domain.review.wconceptReview;

import Designovel.Capstone.api.styleFilter.dto.ReviewTrendDTO;
import Designovel.Capstone.domain.review.reviewProduct.ReviewStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WConceptReviewService {
    private final WConceptReviewRepository wConceptReviewRepository;
    private final ReviewStyleService reviewStyleService;
    public Page<WConceptReview> findByStyleId(String styleId, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return wConceptReviewRepository.findByStyleId(styleId, pageable);
    }

    public List<ReviewTrendDTO> getWConceptReviewTrend(String styleId) {
        List<Object[]> queryResult = wConceptReviewRepository.findReviewCountByStyleId(styleId);
        return reviewStyleService.processReviewTrendQueryResult(queryResult);
    }
}
