package Designovel.Capstone.domain.style.styleRanking;

import Designovel.Capstone.domain.category.categoryStyle.CategoryStyle;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "style_ranking")
public class StyleRanking {

    @Id
    @Column(name = "rank_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer rankId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "style_id", referencedColumnName = "style_id"),
            @JoinColumn(name = "category_id", referencedColumnName = "category_id"),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id")
    })
    private CategoryStyle categoryStyle;

    @Column(name = "style_id", insertable = false, updatable = false)
    private String styleId;

    @Column(name = "category_id", insertable = false, updatable = false)
    private Integer categoryId;

    @Column(name = "mall_type_id", insertable = false, updatable = false)
    private String mallTypeId;
    @Column(name = "brand")
    private String brand;

    @Column(name = "rank_score")
    private float rankScore;

    @Column(name = "style_name")
    private String styleName;

    @Column(name = "fixed_price")
    private Integer fixedPrice;

    @Column(name = "discounted_price")
    private Integer discountedPrice;

    @Column(name = "monetary_unit")
    private String monetaryUnit;

    @Column(name = "crawled_date")
    private LocalDate crawledDate;
}
