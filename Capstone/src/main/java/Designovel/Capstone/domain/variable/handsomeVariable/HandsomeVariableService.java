package Designovel.Capstone.domain.variable.handsomeVariable;

import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HandsomeVariableService {

    private final HandsomeVariableRepository handsomeVariableRepository;

    public HandsomeVariable getHandsomeVariableByProductId(String productId) {
        return handsomeVariableRepository.findByProduct_Id_ProductId(productId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.VARIABLE_NOT_FOUND_ID));
    }
}
