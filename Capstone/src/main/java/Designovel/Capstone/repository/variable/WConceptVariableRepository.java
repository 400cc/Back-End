package Designovel.Capstone.repository.variable;

import Designovel.Capstone.entity.WConceptVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WConceptVariableRepository extends JpaRepository<WConceptVariable, Integer> {
}
