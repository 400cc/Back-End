package Designovel.Capstone.service.variable;

import Designovel.Capstone.entity.HandsomeVariable;
import Designovel.Capstone.exception.CustomException;
import Designovel.Capstone.exception.ErrorCode;
import Designovel.Capstone.repository.variable.HandsomeVariableRepository;
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
