package Designovel.Capstone.domain.variable.wconceptVariable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WConceptVariableRepository extends JpaRepository<WConceptVariable, Integer> {
    Optional<WConceptVariable> findByStyle_Id_StyleId(String styleId);
}
