package Designovel.Capstone.domain.style.styleRanking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class StyleRankingService {

    private final StyleRankingRepository styleRankingRepository;

    public List<String> getBrandsByMallTypeId(String mallTypeId) {
        return styleRankingRepository.findDistinctBrand(mallTypeId);
    }

}
