package Designovel.Capstone.domain.mallType.enumType;

import lombok.Getter;

@Getter
public enum MallTypeId {
    MUSINSA("JN1qnDZA"),
    WCONCEPT("l8WAu4fP"),
    HANDSOME("FHyETFQN");

    private String type;

    MallTypeId(String type) {
        this.type = type;
    }
}
