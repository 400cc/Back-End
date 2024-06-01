package Designovel.Capstone.service.variable;

import Designovel.Capstone.entity.MusinsaVariable;
import Designovel.Capstone.exception.CustomException;
import Designovel.Capstone.exception.ErrorCode;
import Designovel.Capstone.repository.variable.MusinsaVariableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusinsaVariableService {
    private final MusinsaVariableRepository musinsaVariableRepository;

    public MusinsaVariable getMusinsaVariable(String productId) {
        return musinsaVariableRepository.findByProduct_Id_ProductId(productId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.VARIABLE_NOT_FOUND_ID));
    }
}
