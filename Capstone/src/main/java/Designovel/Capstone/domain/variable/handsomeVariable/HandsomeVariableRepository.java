package Designovel.Capstone.domain.variable.handsomeVariable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HandsomeVariableRepository extends JpaRepository<HandsomeVariable, Integer> {
    //한섬 스타일의 쇼핑몰 고유 변수 DB 조회 메서드
    Optional<HandsomeVariable> findByStyle_Id_StyleId(String styleId);
}
