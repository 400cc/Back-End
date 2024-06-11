package Designovel.Capstone.repository.review;

import Designovel.Capstone.entity.ReviewProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewProductRepository extends JpaRepository<ReviewProduct, Integer> {

}
