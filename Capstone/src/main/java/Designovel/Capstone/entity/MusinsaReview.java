package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "musinsa_review")
public class MusinsaReview {

    @Id
    @OneToOne
    @JoinColumn(name = "review_id")
    private ReviewProduct reviewProduct;

    @Column(name = "org_review_id")
    private String orgReviewId;

    private Integer rate;
    private String reviewType;
    private String userInfo;
    private String metaData;
    private String body;
    private Integer helpful;
    private Integer goodStyle;

}

