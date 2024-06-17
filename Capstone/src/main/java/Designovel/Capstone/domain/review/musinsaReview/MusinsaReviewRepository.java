package Designovel.Capstone.domain.review.musinsaReview;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MusinsaReviewRepository extends JpaRepository<MusinsaReview, Integer> {
    Page<MusinsaReview> findByStyleId(String styleId, Pageable pageable);

    @Query("select r.writtenDate, count(r) from MusinsaReview r " +
            "where r.styleId = :styleId " +
            "group by r.writtenDate " +
            "order by r.writtenDate asc")
    List<Object[]> findReviewCountByStyleId(@Param("styleId") String styleId);
}
