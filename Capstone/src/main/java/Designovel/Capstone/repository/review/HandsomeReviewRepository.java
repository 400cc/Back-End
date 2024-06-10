package Designovel.Capstone.repository.review;

import Designovel.Capstone.domain.HandsomeReviewDTO;
import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.entity.id.ReviewProductId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;


@Repository
public interface HandsomeReviewRepository extends JpaRepository<HandsomeReview, ReviewProductId> {

    Page<HandsomeReview> findByProductId(String productId, Pageable pageable);

    @Query("select r.rating, count(r) from HandsomeReview r " +
            "where r.productId = :productId and r.writtenDate >= :startDate " +
            "group by r.rating " +
            "order by r.rating")
    List<Object[]> findReviewCountByProductIdAndWrittenDate(@Param("productId") String productId, @Param("startDate") Date startDate);

    @Query("select new Designovel.Capstone.domain.HandsomeReviewDTO(r) from HandsomeReview r " +
            "where r.productId = :productId and r.writtenDate >= :startDate and r.rating = :rate " +
            "order by r.writtenDate desc ")
    Page<HandsomeReviewDTO> findByProductIdAndRateAndWrittenDate(@Param("productId") String productId, @Param("startDate") Date startDate, @Param("rate") int rate, Pageable pageable);
}
