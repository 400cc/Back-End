package Designovel.Capstone.domain.review.handsomeReview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HandsomeReviewDTO {

    private int reviewId;
    private String styleId;

    private String orgReviewId;

    private Integer rate;

    private LocalDate writtenDate;

    private String userId;

    private String body;

    private String styleColor;

    private String styleSize;

    private String importSource;

    private Integer userHeight;

    private Integer userSize;

    public HandsomeReviewDTO(HandsomeReview review) {
        this.reviewId = review.getReviewId();
        this.styleId = review.getStyleId();
        this.orgReviewId = review.getOrgReviewId();
        this.rate = review.getRate();
        this.writtenDate = review.getWrittenDate();
        this.userId = review.getUserId();
        this.body = review.getBody();
        this.styleColor = review.getStyleColor();
        this.styleSize = review.getStyleSize();
        this.importSource = review.getImportSource();
        this.userHeight = review.getUserHeight();
        this.userSize = review.getUserSize();
    }

}
