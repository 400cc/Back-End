package Designovel.Capstone.service.review;

import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.repository.review.HandsomeReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandsomeReviewService {
    private final HandsomeReviewRepository handsomeReviewRepository;

    public Page<HandsomeReview> findByProductId(String productId, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return handsomeReviewRepository.findByProductId(productId, pageable);
    }
}
