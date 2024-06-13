package Designovel.Capstone.domain.review.wconceptReview;

import Designovel.Capstone.domain.review.reviewProduct.ReviewStyle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "wconcept_review")
public class WConceptReview {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "review_id")
    @JsonIgnore
    private ReviewStyle reviewStyle;

    @Column(name = "style_id", nullable = false)
    private String styleId;

    @Column(name = "org_review_id", unique = true, nullable = false)
    private String orgReviewId;
    @Column(name = "purchase_option")
    private String purchaseOption;
    @Column(name = "size_info")
    private String sizeInfo;
    @Column(name = "size")
    private String size;
    @Column(name = "material")
    private String material;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "written_date")
    private LocalDate writtenDate;
    @Column(name = "body")
    private String body;
    @Column(name = "rate")
    private Integer rate;
    @Column(name = "likes")
    private Integer likes;

}
