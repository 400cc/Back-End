package Designovel.Capstone.domain.product.productRanking;

import Designovel.Capstone.domain.category.categoryProduct.CategoryProduct;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id")
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
    private LocalDate crawledDate;
}
