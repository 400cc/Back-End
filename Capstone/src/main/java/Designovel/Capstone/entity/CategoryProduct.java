package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.CategoryProductId;
import jakarta.persistence.*;

@Entity
public class CategoryProduct {

    @EmbeddedId
    private CategoryProductId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false)
    })
    private Product product;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", insertable=false, updatable=false)
    private Category category;
}
