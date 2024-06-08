package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.ProductId;
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
    private MallType mallType;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Image> images;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<CategoryProduct> categoryProducts;
}