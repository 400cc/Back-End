package Designovel.Capstone.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class ProductRanking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rankId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id"),
            @JoinColumn(name = "mall_type")
    })
    private Product product;
    private String brand;
    private float rankScore;
    private Integer fixedPrice;
    private Integer discountedPrice;
    private String monetaryUnit;
    private Date crawledDate;
}
