package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.CategoryProductId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category_product")
public class CategoryProduct {

    @EmbeddedId
    private CategoryProductId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    @JsonIgnore
    private Product product;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", insertable=false, updatable=false)
    private Category category;
}
