package Designovel.Capstone.api.styleFilter.service;

import Designovel.Capstone.api.styleFilter.dto.DupeExposureIndex;
import Designovel.Capstone.api.styleFilter.dto.StyleFilterDTO;
import Designovel.Capstone.api.styleFilter.queryDSL.StyleFilterQueryDSL;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.category.category.CategoryDTO;
import Designovel.Capstone.domain.category.category.QCategory;
import Designovel.Capstone.domain.image.ImageDTO;
import Designovel.Capstone.domain.image.QImage;
import Designovel.Capstone.domain.style.style.StyleId;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Slf4j
@Service
@RequiredArgsConstructor
public class StyleFilterService {

    private final StyleFilterQueryDSL styleFilterQueryDSL;

    /**
     * 동적 필터에 따라 상품 기본 정보, 노출 지수, 가격 등 상품들 Pagination으로 반환
     * 1. 동적 필터 생성
     * 2. 노출 지수 DB 조회 후 가져오기
     * 3. 값 할당을 위한 Map 생성
     * 4. 각 상품들의 최신 가격 및 기타 정보 DB 조회
     * 5. 값 할당 후 반환
     * @param filter
     * @param page
     * @return Pagination 형태로 값, 전체 개수를 설정하여 반환
     */
    public Page<StyleRankingDTO> getStyleRankingByFilter(StyleFilterDTO filter, int page) {
        // 동적 필터 생성
        int size = 20; // 페이지당 항목 수 고정
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = styleFilterQueryDSL.buildStyleFilter(filter);

        //노출 지수 가져오기
        List<Tuple> exposureIndexQueryResult = styleFilterQueryDSL.getExposureIndexInfo(builder, pageable, filter.getSortBy(), filter.getSortOrder());
        //페이지 전체 개수 조회
        long total = styleFilterQueryDSL.getFilteredStyleCount(builder);

        //값 할당을 위한 Map 생성
        Map<String, StyleRankingDTO> styleRankingMap = createStyleRankingDTOMap(exposureIndexQueryResult);
        List<StyleId> styleIdList = getStyleIdList(exposureIndexQueryResult);

        //가격 정보 및 기타 정보 조회
        List<Tuple> priceQueryResult = styleFilterQueryDSL.getPriceInfo(builder, styleIdList, filter);
        //가격에 대한 값 할당
        updateStylePrices(styleRankingMap, priceQueryResult);

        List<StyleRankingDTO> resultList = new ArrayList<>(styleRankingMap.values());
        return new PageImpl<>(resultList, pageable, total);
    }


    /**
     * 노출 지수 조회 결과를 통해 StyleRankingDTO 및 Map 생성 메서드
     * - 조회 결과로 StyleRankingDTO 생성
     * - Map Key를 생성 후 Map에 담아 반환
     * @param rankScoreResult
     * @return 생성한 Map 반환
     */
    private Map<String, StyleRankingDTO> createStyleRankingDTOMap(List<Tuple> rankScoreResult) {
        Map<String, StyleRankingDTO> styleRankingDTOMap = new LinkedHashMap<>();
        for (Tuple tuple : rankScoreResult) {
            String styleId = tuple.get(styleRanking.styleId);
            String mallTypeId = tuple.get(styleRanking.mallTypeId);
            Category category = tuple.get(QCategory.category);
            Float exposureIndex = tuple.get(styleRanking.rankScore.sum());
            String styleKey = generateStyleKey(styleId, mallTypeId);

            addDuplicateExposureIndex(styleRankingDTOMap, styleKey, styleId, mallTypeId, exposureIndex, category);
        }
        return styleRankingDTOMap;
    }


    /**
     * Map Key 생성 메서드
     * - 스타일아이디_쇼핑몰아이디 로 구성
     * @param styleId
     * @param mallTypeId
     * @return
     */
    public String generateStyleKey(String styleId, String mallTypeId) {
        return styleId + "_" + mallTypeId;
    }


    /**
     * DB 조회 결과 List<StyleId> 변환 메서드
     * @param exposureIndexQueryResult
     * @return
     */
    public List<StyleId> getStyleIdList(List<Tuple> exposureIndexQueryResult) {
        return exposureIndexQueryResult.stream().map(tuple -> new StyleId(tuple.get(styleRanking.styleId), tuple.get(styleRanking.mallTypeId))).collect(Collectors.toList());
    }

    /**
     * 중복 노출 지수 처리 메서드
     * - Map에 Key가 있다면, 중복 노출 지수 리스트(DupeExposureIndexList)에 추가
     * - 없다면 해당 Map에 Key, Value를 넣음
     * @param styleRankingDTOMap
     * @param styleKey
     * @param styleId
     * @param mallTypeId
     * @param exposureIndex
     * @param category
     */
    private void addDuplicateExposureIndex(Map<String, StyleRankingDTO> styleRankingDTOMap, String styleKey, String styleId, String mallTypeId, Float exposureIndex, Category category) {
        if (styleRankingDTOMap.containsKey(styleKey)) {
            styleRankingDTOMap.get(styleKey).getDupeExposureIndexList().add(new DupeExposureIndex(styleId, mallTypeId, exposureIndex, category));
        } else {
            StyleRankingDTO styleRankingDTO = new StyleRankingDTO(styleId, mallTypeId, category, exposureIndex);
            styleRankingDTOMap.put(styleKey, styleRankingDTO);
        }
    }

    /**
     * 가격 조회 후, StyleRankingDTO에 가격 설정 메서드
     * - Key를 이용하여 해당 상품에 가격 설정
     * - 실제 값을 할당하는 로직은 setStyleRankingDTOData 메서드에 구현
     * @param styleRankingDTOMap
     * @param priceQueryResult
     */
    private void updateStylePrices(Map<String, StyleRankingDTO> styleRankingDTOMap, List<Tuple> priceQueryResult) {
        for (Tuple tuple : priceQueryResult) {
            String styleId = tuple.get(styleRanking.styleId);
            String mallTypeId = tuple.get(category).getMallType().getMallTypeId();
            String styleKey = generateStyleKey(styleId, mallTypeId);
            StyleRankingDTO styleRankingDTO = styleRankingDTOMap.get(styleKey);

            if (styleRankingDTO != null && styleRankingDTO.getDiscountedPrice() == null) {
                setStyleRankingDTOData(tuple, styleRankingDTO);
            }
        }
    }

    /**
     * 가격 조회 값 Set 하는 메서드
     * @param tuple
     * @param styleRankingDTO
     */
    private void setStyleRankingDTOData(Tuple tuple, StyleRankingDTO styleRankingDTO) {
        styleRankingDTO.setImage(new ImageDTO(tuple.get(QImage.image)));
        styleRankingDTO.setBrand(tuple.get(styleRanking.brand));
        styleRankingDTO.setStyleName(tuple.get(styleRanking.styleName));
        styleRankingDTO.setDiscountedPrice(tuple.get(styleRanking.discountedPrice));
        styleRankingDTO.setFixedPrice(tuple.get(styleRanking.fixedPrice));
        styleRankingDTO.setMonetaryUnit(tuple.get(styleRanking.monetaryUnit));
        styleRankingDTO.setCategory(new CategoryDTO(tuple.get(category)));
    }

}
