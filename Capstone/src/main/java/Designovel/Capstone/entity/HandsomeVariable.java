package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "handsome_variable")
public class HandsomeVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer variableId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false)
    })
    private Product product;


    private String productInfo;
    private String fittingInfo;
    private String additionalInfo;

}