package Designovel.Capstone.repository.review;

import Designovel.Capstone.entity.MusinsaReview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface MusinsaReviewRepository extends JpaRepository<MusinsaReview, Integer> {
    Page<MusinsaReview> findByProductId(String productId, Pageable pageable);

}
