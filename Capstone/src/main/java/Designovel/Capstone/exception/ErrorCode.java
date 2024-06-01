package Designovel.Capstone.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
    VARIABLE_NOT_FOUND_ID("해당 상품ID의 Variable이 존재하지 않습니다");
    private final String errorMessage;

    ErrorCode(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
