package Designovel.Capstone.domain.review.musinsaReview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusinsaReviewRepository extends JpaRepository<MusinsaReview, Integer> {
    Page<MusinsaReview> findByStyleId(String styleId, Pageable pageable);

}
