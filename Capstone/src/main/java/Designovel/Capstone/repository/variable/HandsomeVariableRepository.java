package Designovel.Capstone.repository.variable;

import Designovel.Capstone.entity.HandsomeVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HandsomeVariableRepository extends JpaRepository<HandsomeVariable, Integer> {
    Optional<HandsomeVariable> findByProduct_Id_ProductId(String productId);
}
