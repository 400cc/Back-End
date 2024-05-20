package Designovel.Capstone.entity;

import Designovel.Capstone.entity.id.CategoryClosureId;
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

    @Column(name = "mall_type")
    private String mallType;

    @Column(name = "depth")
    private Integer depth;


}
