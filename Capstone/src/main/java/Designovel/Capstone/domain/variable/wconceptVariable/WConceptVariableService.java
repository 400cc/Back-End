package Designovel.Capstone.domain.variable.wconceptVariable;

import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WConceptVariableService {
    private final WConceptVariableRepository wConceptVariableRepository;

    public WConceptVariable getWConceptVariableByStyleId(String styleId) {
        return wConceptVariableRepository.findByStyle_Id_StyleId(styleId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.VARIABLE_NOT_FOUND_ID));
    }
}
