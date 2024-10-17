package Designovel.Capstone.domain.review.wconceptReview;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WConceptReviewRepository extends JpaRepository<WConceptReview, Integer>, CustomWConceptReviewRepository {

    /**
     * W컨셉 Review Trend DB 조회 메서드
     * @param styleId
     * @return 작성일, 해당 작성일의 리뷰 수 반환
     */
    @Query("select r.writtenDate, count(r) from WConceptReview r " +
            "where r.styleId = :styleId " +
            "group by r.writtenDate " +
            "order by r.writtenDate asc")
    List<Object[]> findReviewCountByStyleId(@Param("styleId") String styleId);
}
