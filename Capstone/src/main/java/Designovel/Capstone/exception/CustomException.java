package Designovel.Capstone.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private HttpStatus httpStatus;
    private ErrorCode errorCode;

    public CustomException(HttpStatus httpStatus, ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.httpStatus = httpStatus;
    }

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
