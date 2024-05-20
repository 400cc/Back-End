package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.ReviewProductId;
import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "review_product")
public class ReviewProduct {

    @EmbeddedId
    private ReviewProductId id;

    private Date crawledDate;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false)
    })
    private Product product;

}
