package Designovel.Capstone.domain.review.musinsaReview;
import Designovel.Capstone.api.styleFilter.dto.ReviewCountDTO;
import Designovel.Capstone.api.styleFilter.dto.ReviewFilterDTO;
import Designovel.Capstone.api.styleFilter.dto.ReviewTrendDTO;
import Designovel.Capstone.domain.review.handsomeReview.HandsomeReviewDTO;
import Designovel.Capstone.domain.review.reviewProduct.ReviewStyleService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MusinsaReviewService {
    private final MusinsaReviewRepository musinsaReviewRepository;
    private final ReviewStyleService reviewStyleService;
    public Page<MusinsaReview> findByStyleId(String styleId, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return musinsaReviewRepository.findByStyleId(styleId, pageable);
    }

    public List<ReviewTrendDTO> getMusinsaReviewTrend(String styleId) {
        List<Object[]> queryResult = musinsaReviewRepository.findReviewCountByStyleId(styleId);
        return reviewStyleService.processReviewTrendQueryResult(queryResult);
    }
}
