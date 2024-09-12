package Designovel.Capstone.domain.review.musinsaReview;

import Designovel.Capstone.domain.review.reviewProduct.ReviewStyle;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

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
    private ReviewStyle reviewStyle;
    @Column(name = "style_id", nullable = false)
    private String styleId;

    @Column(name = "org_review_id", unique = true, nullable = false)
    private String orgReviewId;

    @Column(name = "written_date")
    private LocalDate writtenDate;

    @Column(name = "rate")
    private Integer rate;

    @Column(name = "user_info")
    private String userInfo;

    @Column(name = "likes")
    private Integer likes;

    @Column(name = "body")
    private String body;


}

