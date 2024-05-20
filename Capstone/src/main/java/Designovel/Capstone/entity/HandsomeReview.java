package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "handsome_review")
public class HandsomeReview {


    @Id
    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false),
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "review_id", referencedColumnName = "review_id", insertable = false, updatable = false)
    })
    private ReviewProduct reviewProduct;

    private Integer rating;
    private Date writtenDate;
    private String userId;
    private String body;
    private String productColor;
    private String productSize;
    private String importSource;
    private Integer userHeight;
    private Integer userSize;

}
