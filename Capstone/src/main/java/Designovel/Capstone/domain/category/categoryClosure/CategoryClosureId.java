package Designovel.Capstone.domain.category.categoryClosure;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CategoryClosureId implements Serializable {
    @Column(name = "ancestor_id")
    private Integer ancestorId;
    @Column(name = "descendant_id")
    private Integer descendantId;
}