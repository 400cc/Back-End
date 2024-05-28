package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.ReviewProductId;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "review_product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"org_review_id", "mall_type"})
})
public class ReviewProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "org_review_id")
    private String orgReviewId;

    @Column(name = "crawled_date")
    private Date crawledDate;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false)
    })
    private Product product;

}
