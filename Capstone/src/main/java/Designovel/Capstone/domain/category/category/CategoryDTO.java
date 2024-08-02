package Designovel.Capstone.domain.category.category;

import Designovel.Capstone.domain.mallType.MallType;
import Designovel.Capstone.domain.mallType.MallTypeDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CategoryDTO {

    private Integer categoryId;
    private String orgCategoryId;
    private String name;
    private MallTypeDTO mallType;

    public CategoryDTO(Category category, MallType mallType) {
        this.categoryId = category.getCategoryId();
        this.orgCategoryId = category.getOrgCategoryId();
        this.name = category.getName();
        this.mallType = new MallTypeDTO(mallType);
    }

    public CategoryDTO(Category category) {
        this.categoryId = category.getCategoryId();
        this.orgCategoryId = category.getOrgCategoryId();
        this.name = category.getName();
        this.mallType = new MallTypeDTO(category.getMallType());
    }

}
