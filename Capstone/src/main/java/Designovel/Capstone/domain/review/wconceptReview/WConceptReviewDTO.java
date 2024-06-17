package Designovel.Capstone.domain.review.wconceptReview;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WConceptReviewDTO {

    private int reviewId;

    private String styleId;

    private String orgReviewId;

    private String purchaseOption;

    private String sizeInfo;

    private String size;

    private String material;

    private String userId;

    private LocalDate writtenDate;

    private String body;

    private Integer rate;

    private Integer likes;
}
