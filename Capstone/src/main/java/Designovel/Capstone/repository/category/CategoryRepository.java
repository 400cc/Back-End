package Designovel.Capstone.repository.category;

import Designovel.Capstone.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByMallType_MallTypeId(String mallTypeId);
}
