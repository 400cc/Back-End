package Designovel.Capstone.exception;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.ResponseEntity;

@Data
@Builder
public class ErrorDTO {
    private String errorMessage;

    public static ResponseEntity<ErrorDTO> toResponseEntity(CustomException ex) {
        ErrorCode errorCode = ex.getErrorCode();

        return ResponseEntity
                .status(ex.getHttpStatus())
                .body(ErrorDTO.builder()
                        .errorMessage(errorCode.getErrorMessage())
                        .build());
    }
}
