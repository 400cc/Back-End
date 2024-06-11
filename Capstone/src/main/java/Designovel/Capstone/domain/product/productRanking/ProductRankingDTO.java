package Designovel.Capstone.domain.product.productRanking;

import Designovel.Capstone.api.productFilter.dto.DupeExposureIndex;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.product.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRankingDTO {
    private String productId;
    private String mallType;
    private String brand;
    private Integer discountedPrice;
    private Integer fixedPrice;
    private Float exposureIndex;
    private String monetaryUnit;
    private Image image;
    private Category category;
    private List<DupeExposureIndex> dupeExposureIndexList;

    public ProductRankingDTO(Product product, String brand, Float exposureIndex) {
        this.productId = product.getId().getProductId();
        this.mallType = product.getId().getMallTypeId();
        this.image = java.util.Optional.ofNullable(product.getImages())
                .filter(images -> !images.isEmpty())
                .map(images -> images.get(0))
                .orElse(null);
        this.brand = brand;
        this.exposureIndex = exposureIndex;
        this.dupeExposureIndexList = new ArrayList<>();
    }

}
