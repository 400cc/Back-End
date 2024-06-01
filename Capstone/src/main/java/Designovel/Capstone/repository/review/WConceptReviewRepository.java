package Designovel.Capstone.repository.review;

import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.entity.WConceptReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WConceptReviewRepository extends JpaRepository<WConceptReview, Integer> {
    Page<WConceptReview> findByProductId(String productId, Pageable pageable);
}
