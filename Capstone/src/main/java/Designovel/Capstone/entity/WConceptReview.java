package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "wconcept_review")
public class WConceptReview {

    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false),
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "review_id", referencedColumnName = "review_id", insertable = false, updatable = false)
    })
    private ReviewProduct reviewProduct;

    private String purchaseOption;
    private String sizeInfo;
    private String size;
    private String material;
    private String userId;
    private Date writtenDate;
    private String body;
    private Integer rate;
    private Integer likes;

    // getters and setters
}
