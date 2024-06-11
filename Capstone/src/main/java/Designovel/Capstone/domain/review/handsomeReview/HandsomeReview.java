package Designovel.Capstone.domain.review.handsomeReview;

import Designovel.Capstone.domain.review.reviewProduct.ReviewProduct;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "handsome_review")
public class HandsomeReview {

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
    @Column(name = "rating")
    private Integer rating;
    @Column(name = "written_date")
    private LocalDate writtenDate;
    @Column(name = "user_id")
    private String userId;
    @Column(name = "body")
    private String body;
    @Column(name = "product_color")
    private String productColor;
    @Column(name = "product_size")
    private String productSize;
    @Column(name = "import_source")
    private String importSource;
    @Column(name = "user_height")
    private Integer userHeight;
    @Column(name = "user_size")
    private Integer userSize;

}
