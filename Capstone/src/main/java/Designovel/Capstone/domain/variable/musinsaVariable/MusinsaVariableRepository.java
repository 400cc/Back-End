package Designovel.Capstone.domain.variable.musinsaVariable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MusinsaVariableRepository extends JpaRepository<MusinsaVariable, Integer> {
    //무신사 스타일의 쇼핑몰 고유 변수 DB 조회 메서드
    @Query("select v from MusinsaVariable v where v.style.id.styleId = :styleId")
    Optional<MusinsaVariable> findByStyleId(@Param("styleId") String styleId);
}
