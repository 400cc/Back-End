package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "musinsa_review")
public class MusinsaReview {

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false),
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "review_id", referencedColumnName = "review_id", insertable = false, updatable = false)
    })
    private ReviewProduct reviewProduct;

    private Integer rate;
    private String reviewType;
    private String userInfo;
    private String metaData;
    private String body;
    private Integer helpful;
    private Integer goodStyle;

}

