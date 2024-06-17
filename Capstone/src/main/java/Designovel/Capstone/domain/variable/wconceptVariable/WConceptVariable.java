package Designovel.Capstone.domain.variable.wconceptVariable;

import Designovel.Capstone.domain.style.style.Style;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "wconcept_variable")
public class WConceptVariable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer variableId;

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "style_id", referencedColumnName = "style_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    @JsonIgnore
    private Style style;

    @Column(name = "likes")
    private Integer likes;
    @Column(name = "sold_out")
    private Boolean soldOut;

}
