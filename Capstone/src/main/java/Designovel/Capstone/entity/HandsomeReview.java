package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "handsome_review")
public class HandsomeReview {

    @Id
    @OneToOne
    @JoinColumn(name = "review_id")
    private ReviewProduct reviewProduct;

    @Column(name = "org_review_id")
    private String orgReviewId;
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
