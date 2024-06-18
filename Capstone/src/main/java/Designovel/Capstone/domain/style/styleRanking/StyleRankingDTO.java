package Designovel.Capstone.domain.style.styleRanking;

import Designovel.Capstone.api.styleFilter.dto.DupeExposureIndex;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.image.Image;
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
    private Image image;
    private Category category;
    private List<DupeExposureIndex> dupeExposureIndexList;

    public StyleRankingDTO(Style style, String brand, Float exposureIndex) {
        this.styleId = style.getId().getStyleId();
        this.mallTypeId = style.getId().getMallTypeId();
        this.image = java.util.Optional.ofNullable(style.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0))
                .orElse(null);
        this.brand = brand;
        this.exposureIndex = exposureIndex;
        this.dupeExposureIndexList = new ArrayList<>();
    }

}
