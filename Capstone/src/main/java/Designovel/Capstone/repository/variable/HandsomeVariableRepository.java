package Designovel.Capstone.repository.variable;

import Designovel.Capstone.entity.HandsomeVariable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HandsomeVariableRepository extends JpaRepository<HandsomeVariable, Integer> {
}
