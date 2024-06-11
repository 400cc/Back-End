package Designovel.Capstone.domain.variable.wconceptVariable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WConceptVariableRepository extends JpaRepository<WConceptVariable, Integer> {
    Optional<WConceptVariable> findByProduct_Id_ProductId(String productId);
}
