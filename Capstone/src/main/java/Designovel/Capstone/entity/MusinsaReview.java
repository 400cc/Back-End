package Designovel.Capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "musinsa_review")
public class MusinsaReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private ReviewProduct reviewProduct;
    @Column(name = "product_id", nullable = false)
    private String productId;

    @Column(name = "org_review_id", unique = true, nullable = false)
    private String orgReviewId;

    @Column(name = "rate")
    private Integer rate;
    @Column(name = "review_type")
    private String reviewType;
    @Column(name = "user_info")
    private String userInfo;
    @Column(name = "meta_data")
    private String metaData;
    @Column(name = "body")
    private String body;
    @Column(name = "helpful")
    private Integer helpful;
    @Column(name = "good_style")
    private Integer goodStyle;

}

