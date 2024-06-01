package Designovel.Capstone.repository.variable;

import Designovel.Capstone.entity.HandsomeVariable;
import Designovel.Capstone.entity.WConceptVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WConceptVariableRepository extends JpaRepository<WConceptVariable, Integer> {
    Optional<WConceptVariable> findByProduct_Id_ProductId(String productId);
}
