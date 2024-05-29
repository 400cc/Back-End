package Designovel.Capstone.repository.product;

import Designovel.Capstone.entity.SKUAttribute;
import Designovel.Capstone.entity.id.ProductId;
import Designovel.Capstone.service.product.SKUAttributeService;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SKUAttributeRepository extends JpaRepository<SKUAttribute, Integer> {

    List<SKUAttribute> findByProduct_Id(ProductId productId);
}
