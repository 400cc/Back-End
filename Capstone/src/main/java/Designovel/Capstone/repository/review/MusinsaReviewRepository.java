package Designovel.Capstone.repository.review;
import Designovel.Capstone.entity.MusinsaReview;
import Designovel.Capstone.entity.WConceptReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusinsaReviewRepository extends JpaRepository<MusinsaReview, Integer> {
    Page<MusinsaReview> findByProductId(String productId, Pageable pageable);
}
