package Designovel.Capstone.domain;

import Designovel.Capstone.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRankingAvgDTO {
    private String productId;
    private String mallType;
    private String brand;
    private Integer discountedPrice;
    private Integer fixedPrice;
    private Float exposureIndex;
    private String monetaryUnit;
    private Image image;
    private Category category;

    public ProductRankingAvgDTO(Product product, String brand, Float exposureIndex) {
        this.productId = product.getId().getProductId();
        this.mallType = product.getId().getMallType();
        this.image = product.getImages() != null && !product.getImages().isEmpty()
                ? product.getImages().get(0)
                : null;
        this.brand = brand;
        this.exposureIndex = exposureIndex;
    }

}
