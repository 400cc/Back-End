package Designovel.Capstone.domain.category.categoryStyle;

import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.style.style.Style;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category_style")
public class CategoryStyle {

    @EmbeddedId
    private CategoryStyleId id;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "style_id", referencedColumnName = "style_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    @JsonIgnore
    private Style style;

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id", insertable=false, updatable=false)
    private Category category;
}
