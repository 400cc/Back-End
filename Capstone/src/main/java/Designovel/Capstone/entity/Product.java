package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.ProductId;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
public class Product {

    @EmbeddedId
    private ProductId product;

}