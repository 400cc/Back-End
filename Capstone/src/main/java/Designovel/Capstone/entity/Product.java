package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.ProductId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Set;

@Entity
@Data
@Table(name = "product")
public class Product {

    @EmbeddedId
    private ProductId id;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<Image> images;

    @OneToMany(mappedBy = "product")
    @ToString.Exclude
    private List<CategoryProduct> categoryProducts;
}