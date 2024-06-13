package Designovel.Capstone.domain.category.categoryStyle;

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
public class CategoryStyleId implements Serializable {

    @Column(name = "category_id")
    private Integer categoryId;
    @Column(name = "style_id")
    private String styleId;
    @Column(name = "mall_type_id")
    private String mallTypeId;

}
