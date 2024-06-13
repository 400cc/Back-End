package Designovel.Capstone.domain.review.wconceptReview;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WConceptReviewService {
    private final WConceptReviewRepository wConceptReviewRepository;

    public Page<WConceptReview> findByStyleId(String styleId, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return wConceptReviewRepository.findByStyleId(styleId, pageable);
    }
}
