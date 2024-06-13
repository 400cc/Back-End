package Designovel.Capstone.domain.variable.handsomeVariable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HandsomeVariableRepository extends JpaRepository<HandsomeVariable, Integer> {
    Optional<HandsomeVariable> findByStyle_Id_StyleId(String styleId);
}
