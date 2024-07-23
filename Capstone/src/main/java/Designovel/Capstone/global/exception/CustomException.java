package Designovel.Capstone.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final ErrorCode errorCode;

    public CustomException(HttpStatus httpStatus, ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public CustomException(ErrorCode errorCode) {
        this.httpStatus = null;
        this.errorCode = errorCode;
    }
}
