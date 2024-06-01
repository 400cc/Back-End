package Designovel.Capstone.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionInterceptor {
    @ExceptionHandler(CustomException.class)
    public final ResponseEntity<ErrorDTO> handleCustomException(CustomException ex, HttpServletRequest request) {
        log.error("CustomException. URL: {}, ErrorMessage: {}", request.getRequestURL(), ex.getErrorCode().getErrorMessage());
        return ErrorDTO.toResponseEntity(ex);
    }
}
