package Designovel.Capstone.api.styleFilter.dto;

import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.category.category.CategoryDTO;
import Designovel.Capstone.domain.style.style.Style;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class DupeExposureIndex {

    private String styleId;
    private String mallTypeId;
    private Float exposureIndex;
    private CategoryDTO category;

    public DupeExposureIndex(String styleId, String mallTypeId, Float exposureIndex, Category category) {
        this.styleId = styleId;
        this.mallTypeId = mallTypeId;
        this.exposureIndex = exposureIndex;
        this.category = new CategoryDTO(category);
    }
}
