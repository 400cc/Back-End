package Designovel.Capstone.domain;

import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.entity.ReviewProduct;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandsomeReviewDTO {

    private int reviewId;
    private String productId;

    private String orgReviewId;

    private Integer rating;

    private LocalDate writtenDate;

    private String userId;

    private String body;

    private String productColor;

    private String productSize;

    private String importSource;

    private Integer userHeight;

    private Integer userSize;

    public HandsomeReviewDTO(HandsomeReview review) {
        this.reviewId = review.getReviewId();
        this.productId = review.getProductId();
        this.orgReviewId = review.getOrgReviewId();
        this.rating = review.getRating();
        this.writtenDate = review.getWrittenDate();
        this.userId = review.getUserId();
        this.body = review.getBody();
        this.productColor = review.getProductColor();
        this.productSize = review.getProductSize();
        this.importSource = review.getImportSource();
        this.userHeight = review.getUserHeight();
        this.userSize = review.getUserSize();
    }

}
