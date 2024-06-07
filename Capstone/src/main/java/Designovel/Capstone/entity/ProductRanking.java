package Designovel.Capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "product_ranking")
public class ProductRanking {

    @Id
    @Column(name = "rank_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rankId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id"),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type")
    })
    private CategoryProduct categoryProduct;

    @Column(name = "brand")
    private String brand;

    @Column(name = "rank_score")
    private float rankScore;

    @Column(name = "fixed_price")
    private Integer fixedPrice;

    @Column(name = "discounted_price")
    private Integer discountedPrice;

    @Column(name = "monetary_unit")
    private String monetaryUnit;

    @Column(name = "crawled_date")
    private Date crawledDate;
}
