package Designovel.Capstone.domain;

import Designovel.Capstone.entity.Category;
import Designovel.Capstone.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DupeExposureIndex {

    private String productId;
    private String mallType;
    private Float exposureIndex;
    private Category category;

    public DupeExposureIndex(Product product, Float exposureIndex, Category category) {
        this.productId = product.getId().getProductId();
        this.mallType = product.getId().getMallType();
        this.exposureIndex = exposureIndex;
        this.category = category;
    }
}
