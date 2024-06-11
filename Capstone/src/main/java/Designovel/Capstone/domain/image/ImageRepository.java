package Designovel.Capstone.domain.image;

import Designovel.Capstone.domain.product.product.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends JpaRepository<Image, Integer> {
    List<Image> findByProduct_Id(ProductId productId);
}
