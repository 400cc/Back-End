package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.ProductId;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;

@Entity
@Data
@Table(name = "product")
public class Product {

    @EmbeddedId
    private ProductId id;

    @OneToMany(mappedBy = "product")
    private Set<Image> images;
}