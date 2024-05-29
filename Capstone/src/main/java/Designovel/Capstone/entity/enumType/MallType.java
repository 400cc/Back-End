package Designovel.Capstone.entity.enumType;

import lombok.Getter;
import lombok.ToString;

@Getter
public enum MallType {
    MUSINSA("MUSINSA"),
    WCONCEPT("WCONCEPT"),
    HANDSOME("HANDSOME");

    private String type;

    MallType(String type) {
        this.type = type;
    }
}
