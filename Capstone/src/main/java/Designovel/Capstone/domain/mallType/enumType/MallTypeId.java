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

    /**
     * 요청 시 Enum에 정의된 쇼핑몰 value가 아니면 Error 발생 메서드
     * - 일치하지 않을 시 400번 에러로 클라이언트에게 응답
     * @param type
     */
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
