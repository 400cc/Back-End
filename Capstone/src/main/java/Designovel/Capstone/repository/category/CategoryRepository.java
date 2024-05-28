package Designovel.Capstone.repository.category;

import Designovel.Capstone.entity.Category;
import Designovel.Capstone.entity.CategoryClosure;
import Designovel.Capstone.repository.querydsl.CustomCategoryRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer>, CustomCategoryRepository {

    List<Category> findByMallType(String mallType);
}
