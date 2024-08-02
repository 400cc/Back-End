package Designovel.Capstone.domain.style.style;

import Designovel.Capstone.domain.category.categoryStyle.CategoryStyle;
import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.mallType.MallType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@Table(name = "style")
public class Style {

    @EmbeddedId
    private StyleId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mall_type_id", insertable = false, updatable = false)
    private MallType mallTypeId;

    @OneToMany(mappedBy = "style")
    @ToString.Exclude
    private List<Image> images;

    @Column(name = "last_crawled_date")
    private LocalDate lastCrawledDate;
}