package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "wconcept_review")
public class WConceptReview {

    @Id
    @OneToOne
    @JoinColumn(name = "review_id")
    private ReviewProduct reviewProduct;

    @Column(name = "org_review_id")
    private String orgReviewId;
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
