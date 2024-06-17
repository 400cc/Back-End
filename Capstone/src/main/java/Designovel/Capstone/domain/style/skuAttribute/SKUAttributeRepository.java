package Designovel.Capstone.domain.style.skuAttribute;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SKUAttributeRepository extends JpaRepository<SKUAttribute, Integer> {

    @Query("select s from SKUAttribute s where s.style.id.styleId = :styleId and s.style.id.mallTypeId = :mallTypeId")
    List<SKUAttribute> findByStyleId(@Param("styleId") String styleId, @Param("mallTypeId") String mallTypeId);
}
