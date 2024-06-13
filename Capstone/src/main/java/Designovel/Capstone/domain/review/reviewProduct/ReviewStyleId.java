package Designovel.Capstone.domain.review.reviewProduct;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ReviewStyleId implements Serializable {
    @Column(name = "review_id")
    private String reviewId;

    @Column(name = "style_id")
    private String styleId;
    @Column(name = "mall_type_id")
    private String mallTypeId;

}