package Designovel.Capstone.domain.style.styleRanking;

import Designovel.Capstone.api.styleFilter.dto.DupeExposureIndex;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.category.category.CategoryDTO;
import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.image.ImageDTO;
import Designovel.Capstone.domain.style.style.Style;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StyleRankingDTO {
    private String styleId;
    private String mallTypeId;
    private String brand;
    private Integer discountedPrice;
    private Integer fixedPrice;
    private String styleName;
    private Float exposureIndex;
    private String monetaryUnit;
    private ImageDTO image;
    private CategoryDTO category;
    private List<DupeExposureIndex> dupeExposureIndexList;

    public StyleRankingDTO(String styleId, String mallTypeId, Category category, Float exposureIndex) {
        this.styleId = styleId;
        this.mallTypeId = mallTypeId;
        this.category = new CategoryDTO(category);
        this.exposureIndex = exposureIndex;
        this.dupeExposureIndexList = new ArrayList<>();
    }

}
