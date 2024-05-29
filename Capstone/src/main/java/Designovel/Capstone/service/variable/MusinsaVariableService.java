package Designovel.Capstone.service.variable;

import Designovel.Capstone.entity.MusinsaVariable;
import Designovel.Capstone.entity.id.ProductId;
import Designovel.Capstone.repository.variable.MusinsaVariableRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MusinsaVariableService {
    private final MusinsaVariableRepository musinsaVariableRepository;

    public MusinsaVariable getMusinsaVariable(String productId) {
        return musinsaVariableRepository.findByProduct_Id_ProductId(productId);

    }
}
