package Designovel.Capstone.repository.review;

import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.entity.id.ReviewProductId;
import Designovel.Capstone.repository.querydsl.CustomHandsomeReviewRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface HandsomeReviewRepository extends JpaRepository<HandsomeReview, ReviewProductId>, CustomHandsomeReviewRepository {

    @Query("select r.writtenDate, count(r) from HandsomeReview r " +
            "where r.productId = :productId " +
            "group by r.writtenDate " +
            "order by r.writtenDate asc")
    List<Object[]> findReviewCountByProductIdAndDate(@Param("productId") String productId);
}
