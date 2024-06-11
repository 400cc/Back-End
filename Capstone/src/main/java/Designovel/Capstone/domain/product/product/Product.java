package Designovel.Capstone.domain.product.product;

import Designovel.Capstone.domain.category.categoryProduct.CategoryProduct;
import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.mallType.MallType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
@Table(name = "product")
public class Product {

    @EmbeddedId
    private ProductId id;
    @ManyToOne
    @JoinColumn(name = "mall_type_id", insertable = false, updatable = false)
    private MallType mallTypeId;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Image> images;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<CategoryProduct> categoryProducts;
}