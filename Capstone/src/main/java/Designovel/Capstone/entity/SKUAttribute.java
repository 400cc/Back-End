package Designovel.Capstone.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "sku_attribute")
public class SKUAttribute {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer skuId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type", insertable = false, updatable = false)
    })
    private Product product;

    private String attrKey;
    private String attrValue;


}