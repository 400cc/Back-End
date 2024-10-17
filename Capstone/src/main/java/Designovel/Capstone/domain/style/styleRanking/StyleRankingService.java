package Designovel.Capstone.domain.style.styleRanking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
@Slf4j
public class StyleRankingService {

    private final StyleRankingRepository styleRankingRepository;

    /**
     * 쇼핑몰에 따른 브랜드 조회 메서드(중복 제거)
     * @param mallTypeId
     * @return 브랜드를 리스트로 반환
     */
    public List<String> getBrandsByMallTypeId(String mallTypeId) {
        return styleRankingRepository.findDistinctBrand(mallTypeId);
    }

}
