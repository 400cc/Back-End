package Designovel.Capstone.entity.id;

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
    @Column(name = "mall_type")
    private String mallType;

    // constructors, getters, setters, hashCode, equals
}
