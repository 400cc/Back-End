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
}
