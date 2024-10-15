package Designovel.Capstone.domain.mallType.enumType;

import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Getter
@Slf4j
public enum MallTypeId {
    MUSINSA("JN1qnDZA"),
    WCONCEPT("l8WAu4fP"),
    HANDSOME("FHyETFQN");

    private String type;

    MallTypeId(String type) {
        this.type = type;
    }

    public static void checkMallTypeId(String type) {
        for (MallTypeId mallTypeId : values()) {
            if (mallTypeId.getType().equals(type)) {
                return;
            }
        }
        log.error("유효하지 않은 쇼핑몰: {}", type);
        throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_MALL_TYPE_ID);
    }
}
