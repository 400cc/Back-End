package Designovel.Capstone.domain.review.wconceptReview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WConceptReviewRepository extends JpaRepository<WConceptReview, Integer>, CustomWConceptReviewRepository {

    @Query("select r.writtenDate, count(r) from WConceptReview r " +
            "where r.styleId = :styleId " +
            "group by r.writtenDate " +
            "order by r.writtenDate asc")
    List<Object[]> findReviewCountByStyleId(@Param("styleId") String styleId);
}
