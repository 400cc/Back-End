package Designovel.Capstone.domain.image;

import Designovel.Capstone.domain.style.style.Style;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "style_id", referencedColumnName = "style_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    @JsonIgnore
    private Style style;

    @Column(name = "url")
    private String url;
    @Column(name = "sequence")
    private Integer sequence;

}
