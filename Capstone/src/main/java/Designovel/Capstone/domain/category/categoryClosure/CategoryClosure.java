package Designovel.Capstone.domain.category.categoryClosure;

import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.mallType.MallType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Table;

@Entity
@Data
@NoArgsConstructor
@Table(name = "category_closure")
public class CategoryClosure {
    @EmbeddedId
    private CategoryClosureId id;

    @MapsId("ancestorId")
    @ManyToOne
    @JoinColumn(name = "ancestor_id",referencedColumnName = "categoryId", insertable = false, updatable = false)
    private Category ancestorId;

    @MapsId("descendantId")
    @ManyToOne
    @JoinColumn(name = "descendant_id",referencedColumnName = "categoryId", insertable = false, updatable = false)
    private Category descendantId;

    @ManyToOne
    @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    private MallType mallType;

    @Column(name = "depth")
    private Integer depth;


}
