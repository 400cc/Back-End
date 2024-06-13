package Designovel.Capstone.domain.review.reviewProduct;

import Designovel.Capstone.domain.style.style.Style;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "review_product", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"org_review_id", "mall_type"})
})
public class ReviewStyle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @Column(name = "org_review_id")
    private String orgReviewId;

    @Column(name = "crawled_date")
    private LocalDate crawledDate;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "style_id", referencedColumnName = "style_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    private Style style;

}
