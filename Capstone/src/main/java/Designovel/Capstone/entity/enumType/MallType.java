package Designovel.Capstone.entity.enumType;

import lombok.Getter;

@Getter
public enum MallType {
    MUSINSA("MUSINSA"),
    WCONCEPT("WCONCEPT"),
    HANDSOME("HANDSOME");
    //    TFypT6SD("MUSINSA"),
//    tNGncWzm("WCONCEPT"),
//    rI2sb9p1("HANDSOME");
    private String type;

    MallType(String type) {
        this.type = type;
    }
}
