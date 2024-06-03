package Designovel.Capstone.repository.variable;

import Designovel.Capstone.entity.MusinsaVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusinsaVariableRepository extends JpaRepository<MusinsaVariable, Integer> {

    Optional<MusinsaVariable> findByProduct_Id_ProductId(String productId);
}
