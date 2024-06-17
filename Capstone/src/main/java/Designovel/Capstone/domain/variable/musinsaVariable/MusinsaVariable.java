package Designovel.Capstone.domain.variable.musinsaVariable;


import Designovel.Capstone.domain.style.style.Style;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "musinsa_variable")
public class MusinsaVariable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variable_id")
    private Integer variableId;

    @OneToOne
    @JoinColumns({
            @JoinColumn(name = "style_id", referencedColumnName = "style_id", insertable = false, updatable = false),
            @JoinColumn(name = "mall_type_id", referencedColumnName = "mall_type_id", insertable = false, updatable = false)
    })
    @JsonIgnore
    private Style style;
    
    @Column(name = "male_percentage")
    private Integer malePercentage;
    @Column(name = "female_percentage")
    private Integer femalePercentage;
    @Column(name = "likes")
    private Integer likes;
    @Column(name = "cumulative_sales")
    private Integer cumulativeSales;
    @Column(name = "age_under_18")
    private Integer ageUnder18;
    @Column(name = "age_19_to_23")
    private Integer age19To23;
    @Column(name = "age_24_to_28")
    private Integer age24To28;
    @Column(name = "age_29_to_33")
    private Integer age29To33;
    @Column(name = "age_34_to_39")
    private Integer age34To39;
    @Column(name = "age_over_40")
    private Integer ageOver40;

}
