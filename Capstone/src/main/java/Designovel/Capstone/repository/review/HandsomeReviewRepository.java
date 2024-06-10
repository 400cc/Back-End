package Designovel.Capstone.repository.review;

import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.entity.id.ReviewProductId;
import Designovel.Capstone.repository.querydsl.CustomHandsomeReviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface HandsomeReviewRepository extends JpaRepository<HandsomeReview, ReviewProductId>, CustomHandsomeReviewRepository {

}
