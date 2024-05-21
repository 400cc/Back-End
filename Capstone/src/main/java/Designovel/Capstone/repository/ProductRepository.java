package Designovel.Capstone.repository;

import Designovel.Capstone.entity.Product;
import Designovel.Capstone.entity.id.ProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, ProductId> {
}
