package Designovel.Capstone.service.review;
import Designovel.Capstone.entity.MusinsaReview;
import Designovel.Capstone.entity.WConceptReview;
import Designovel.Capstone.repository.review.MusinsaReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusinsaReviewService {
    private final MusinsaReviewRepository musinsaReviewRepository;
    public Page<MusinsaReview> findByProductId(String productId, int page) {
        int size = 20;
        Pageable pageable = PageRequest.of(page, size);
        return musinsaReviewRepository.findByProductId(productId, pageable);
    }
}
