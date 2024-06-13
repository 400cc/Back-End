package Designovel.Capstone.domain.style.skuAttribute;

import Designovel.Capstone.domain.style.style.StyleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SKUAttributeRepository extends JpaRepository<SKUAttribute, Integer> {

    List<SKUAttribute> findByStyle_Id_StyleId(StyleId styleId);
}
