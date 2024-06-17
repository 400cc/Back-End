package Designovel.Capstone.api.styleFilter.dto;

import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.style.style.Style;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DupeExposureIndex {

    private String productId;
    private String mallType;
    private Float exposureIndex;
    private Category category;

    public DupeExposureIndex(Style style, Float exposureIndex, Category category) {
        this.productId = style.getId().getStyleId();
        this.mallType = style.getId().getMallTypeId();
        this.exposureIndex = exposureIndex;
        this.category = category;
    }
}