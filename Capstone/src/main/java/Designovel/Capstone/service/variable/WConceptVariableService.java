package Designovel.Capstone.service.variable;

import Designovel.Capstone.entity.WConceptVariable;
import Designovel.Capstone.exception.CustomException;
import Designovel.Capstone.exception.ErrorCode;
import Designovel.Capstone.repository.variable.WConceptVariableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WConceptVariableService {
    private final WConceptVariableRepository wConceptVariableRepository;

    public WConceptVariable getWConceptVariableByProductId(String productId) {
        return wConceptVariableRepository.findByProduct_Id_ProductId(productId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.VARIABLE_NOT_FOUND_ID));
    }
}
