package Designovel.Capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "handsome_variable")
public class HandsomeVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variable_id")
    private Integer variableId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    @JsonIgnore
    private Product product;

    @Column(name = "product_info")
    private String productInfo;
    @Column(name = "fitting_info")
    private String fittingInfo;
    @Column(name = "additional_info")
    private String additionalInfo;

}