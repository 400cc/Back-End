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

    private String userInfo;

    private Integer likes;

    private String body;



    public MusinsaReviewDTO(Integer reviewId, String styleId, String orgReviewId, Integer rate, LocalDate writtenDate,
                            String body, int likes, String userInfo) {
        this.reviewId = reviewId;
        this.styleId = styleId;
        this.orgReviewId = orgReviewId;
        this.rate = rate;
        this.writtenDate = writtenDate;
        this.body = body;
        this.likes = likes;
        this.userInfo = userInfo;
    }


}
