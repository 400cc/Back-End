package Designovel.Capstone.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "image")
public class Image {

    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "product_id", referencedColumnName = "product_id"),
            @JoinColumn(name = "mall_type", referencedColumnName = "mall_type")
    })
    @JsonIgnore
    private Product product;

    @Column(name = "url")
    private String url;
    @Column(name = "sequence")
    private Integer sequence;

}
