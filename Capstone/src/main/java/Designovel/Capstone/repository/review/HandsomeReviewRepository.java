package Designovel.Capstone.repository.review;

import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.entity.id.ReviewProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HandsomeReviewRepository extends JpaRepository<HandsomeReview, ReviewProductId> {

    Page<HandsomeReview> findByProductId(String productId, Pageable pageable);
}
