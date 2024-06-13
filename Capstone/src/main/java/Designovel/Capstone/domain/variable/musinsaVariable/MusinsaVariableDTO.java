package Designovel.Capstone.domain.variable.musinsaVariable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MusinsaVariableDTO {
    private Long variableId;
    private String style_id;
    private String mallType;
    private String productNum;
    private double malePercentage;
    private double femalePercentage;
    private int likes;
    private int cumulativeSales;
    private Integer ageUnder18;
    private Integer age19To23;
    private Integer age24To28;
    private Integer age29To33;
    private Integer age34To39;
    private Integer ageOver40;

}
