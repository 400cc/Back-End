package Designovel.Capstone.api.productFilter.dto;

import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.product.product.Product;
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
        this.mallType = product.getId().getMallTypeId();
        this.exposureIndex = exposureIndex;
        this.category = category;
    }
}
