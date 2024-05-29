package Designovel.Capstone.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "sku_attribute")
public class SKUAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sku_id")
    private Integer skuId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false)
    })
    private Product product;

    @Column(name = "attr_key")
    private String attrKey;
    @Column(name = "attr_value")
    private String attrValue;


}