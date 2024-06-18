package Designovel.Capstone.domain.review.musinsaReview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MusinsaReviewDTO {
    private int reviewId;
    private String styleId;

    private String orgReviewId;

    private LocalDate writtenDate;

    private Integer rate;
    private String reviewType;

    private String userInfo;

    private String metaData;

    private String body;

    private Integer helpful;

    private Integer goodStyle;

    public MusinsaReviewDTO(Integer reviewId, String styleId, String orgReviewId, Integer rate, LocalDate writtenDate,
                            String reviewType, String userInfo, String metaData, String body, Integer helpful,
                            Integer goodStyle) {
        this.reviewId = reviewId;
        this.styleId = styleId;
        this.orgReviewId = orgReviewId;
        this.rate = rate;
        this.writtenDate = writtenDate;
        this.reviewType = reviewType;
        this.userInfo = userInfo;
        this.metaData = metaData;
        this.body = body;
        this.helpful = helpful;
        this.goodStyle = goodStyle;
    }


}
