package Designovel.Capstone.domain.style.skuAttribute;

import Designovel.Capstone.domain.style.style.Style;
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
            @JoinColumn(name = "style_id", referencedColumnName = "style_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    private Style style;

    @Column(name = "attr_key")
    private String attrKey;
    @Column(name = "attr_value")
    private String attrValue;


}