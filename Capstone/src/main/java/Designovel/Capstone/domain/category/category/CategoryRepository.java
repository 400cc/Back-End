package Designovel.Capstone.domain.category.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findByMallType_MallTypeId(String mallTypeId);

    @Query("select c.name from Category c where c.categoryId in :categoryIdList")
    List<String> findNameByCategoryIdList(List<Integer> categoryIdList);
}
