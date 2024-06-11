package Designovel.Capstone.domain.category.categoryProduct;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CategoryProductId implements Serializable {

    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "product_id")
    private String productId;
    @Column(name = "mall_type_id")
    private String mallTypeId;

}
