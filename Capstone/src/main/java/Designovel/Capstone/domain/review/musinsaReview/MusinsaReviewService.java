package Designovel.Capstone.domain.review.musinsaReview;
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
