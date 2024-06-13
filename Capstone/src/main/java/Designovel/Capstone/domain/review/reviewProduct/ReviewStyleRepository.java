package Designovel.Capstone.domain.review.reviewProduct;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewStyleRepository extends JpaRepository<ReviewStyle, Integer> {

}
