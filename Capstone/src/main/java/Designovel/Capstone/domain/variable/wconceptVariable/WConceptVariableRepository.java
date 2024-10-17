package Designovel.Capstone.domain.variable.wconceptVariable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WConceptVariableRepository extends JpaRepository<WConceptVariable, Integer> {
    //한섬 스타일의 쇼핑몰 고유 변수 DB 조회 메서드
    Optional<WConceptVariable> findByStyle_Id_StyleId(String styleId);
}
