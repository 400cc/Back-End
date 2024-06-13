package Designovel.Capstone.domain.variable.musinsaVariable;

import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusinsaVariableService {
    private final MusinsaVariableRepository musinsaVariableRepository;

    public MusinsaVariable getMusinsaVariable(String styleId) {
        return musinsaVariableRepository.findByStyle_Id_StyleId(styleId)
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.VARIABLE_NOT_FOUND_ID));
    }
}
