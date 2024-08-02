package Designovel.Capstone.domain.category.category;

import Designovel.Capstone.domain.mallType.MallType;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer categoryId;

    @Column(nullable = false)
    private String orgCategoryId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    private MallType mallType;

    private String name;

}