package Designovel.Capstone.api.clustering.dto;

import Designovel.Capstone.domain.category.category.CategoryDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusteringStyleDTO {
    private String styleId;
    private String styleName;
    private String brand;
    private Integer fixedPrice;
    private Integer discountedPrice;
    private List<CategoryDTO> categoryList;

    public ClusteringStyleDTO(String styleId, String styleName, String brand, int fixedPrice, int discountedPrice) {
        this.styleId = styleId;
        this.styleName = styleName;
        this.brand = brand;
        this.fixedPrice = fixedPrice;
        this.discountedPrice = discountedPrice;
    }
}
