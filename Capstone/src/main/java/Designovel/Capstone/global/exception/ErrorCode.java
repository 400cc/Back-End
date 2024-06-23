package Designovel.Capstone.global.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VARIABLE_NOT_FOUND_ID("해당 스타일 ID의 Variable이 존재하지 않습니다"),
    MALL_TYPE_ID_IS_NULL("쇼핑몰의 ID를 입력해야 합니다."),
    STYLE_ID_OR_PAGE_IS_NULL("스타일 ID와 페이지 번호를 입력해야 합니다."),
    STYLE_ID_IS_NULL("스타일 ID를 입력해야 합니다."),
    PAGE_NUM_IS_NULL("페이지 번호를 입력해야 합니다."),
    STYLE_DETAIL_IS_EMPTY("해당 스타일의 상세 정보가 존재하지 않습니다."),
    INVALID_MALL_TYPE_ID("유효하지 않은 쇼핑몰 ID 입니다.");
    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
