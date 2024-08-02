package Designovel.Capstone.api.styleFilter.service;

import Designovel.Capstone.api.styleFilter.dto.DupeExposureIndex;
import Designovel.Capstone.api.styleFilter.dto.StyleFilterDTO;
import Designovel.Capstone.api.styleFilter.queryDSL.StyleFilterQueryDSL;
import Designovel.Capstone.domain.category.category.CategoryDTO;
import Designovel.Capstone.domain.image.ImageDTO;
import Designovel.Capstone.domain.image.QImage;
import Designovel.Capstone.domain.mallType.MallType;
import Designovel.Capstone.domain.style.style.StyleId;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
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

import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.categoryStyle;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@Slf4j
@Service
@RequiredArgsConstructor
public class StyleFilterService {

    private final StyleFilterQueryDSL styleFilterQueryDSL;

    public void addDuplicateExposureIndex(Map<String, StyleRankingDTO> resultMap, String styleId, String mallTypeId, String brand, Float exposureIndex, CategoryDTO category, String key) {
        if (resultMap.containsKey(key)) {
            DupeExposureIndex dupeExposureIndex = new DupeExposureIndex(styleId, mallTypeId, exposureIndex, category);
            resultMap.get(key).getDupeExposureIndexList().add(dupeExposureIndex);
        } else {
            StyleRankingDTO styleRankingDTO = new StyleRankingDTO(styleId, mallTypeId, brand, exposureIndex);
            resultMap.put(key, styleRankingDTO);
        }
    }

    public String generateStyleKey(String styleId, String mallTypeId) {
        return styleId + "_" + mallTypeId;
    }

    public Page<StyleRankingDTO> getStyleRankingByFilter(StyleFilterDTO filter, int page) {
        int size = 20; // 페이지당 항목 수 고정
        Pageable pageable = PageRequest.of(page, size);
        BooleanBuilder builder = styleFilterQueryDSL.buildStyleFilter(filter);

        //노출 지수 가져오기
        QueryResults<Tuple> styleRankingQueryResult = styleFilterQueryDSL.getExposureIndexInfo(builder, pageable, filter.getSortBy(), filter.getSortOrder());
        List<Tuple> exposureIndexQueryResult = styleRankingQueryResult.getResults();
        long total = styleRankingQueryResult.getTotal();

        //응답 객체 생성
        Map<String, StyleRankingDTO> styleRankingMap = createStyleRankingDTOMap(exposureIndexQueryResult);
        List<StyleId> styleIdList = getStyleIdList(exposureIndexQueryResult);

        //제일 최신 가격(현재가, 할인가) 가져오기
        List<Tuple> priceQueryResult = styleFilterQueryDSL.getPriceInfo(builder, styleIdList, filter);
        updateStylePrices(styleRankingMap, priceQueryResult);

        List<StyleRankingDTO> resultList = new ArrayList<>(styleRankingMap.values());
        return new PageImpl<>(resultList, pageable, total);
    }

    public List<StyleId> getStyleIdList(List<Tuple> exposureIndexQueryResult) {
        return exposureIndexQueryResult.stream()
                .map(tuple -> new StyleId(
                        tuple.get(categoryStyle.id.styleId),
                        tuple.get(categoryStyle.category.mallType).getMallTypeId()
                ))
                .collect(Collectors.toList());
    }


    private Map<String, StyleRankingDTO> createStyleRankingDTOMap(List<Tuple> rankScoreResult) {
        Map<String, StyleRankingDTO> resultMap = new LinkedHashMap<>();
        for (Tuple tuple : rankScoreResult) {
            String styleId = tuple.get(categoryStyle.id.styleId);

            MallType mallType = tuple.get(categoryStyle.category.mallType);
            String mallTypeId = mallType.getMallTypeId();
            String brand = tuple.get(styleRanking.brand);
            Float exposureIndex = tuple.get(styleRanking.rankScore.sum());
            CategoryDTO category = new CategoryDTO(tuple.get(categoryStyle.category), mallType);

            String key = generateStyleKey(styleId, mallTypeId);

            addDuplicateExposureIndex(resultMap, styleId, mallTypeId, brand, exposureIndex, category, key);
        }
        return resultMap;
    }

    private void updateStylePrices(Map<String, StyleRankingDTO> resultMap, List<Tuple> priceResult) {
        log.info(String.valueOf(priceResult.size()));
        for (Tuple tuple : priceResult) {
            String styleId = tuple.get(styleRanking.categoryStyle.id.styleId);
            MallType mallType = tuple.get(categoryStyle.category.mallType);
            String mallTypeId = mallType.getMallTypeId();
            String key = generateStyleKey(styleId, mallTypeId);
            ImageDTO image = new ImageDTO(tuple.get(QImage.image));
            StyleRankingDTO styleRankingDTO = resultMap.get(key);

            if (styleRankingDTO != null && styleRankingDTO.getDiscountedPrice() == null) {
                styleRankingDTO.setImage(image);
                styleRankingDTO.setStyleName(tuple.get(styleRanking.styleName));
                styleRankingDTO.setDiscountedPrice(tuple.get(styleRanking.discountedPrice));
                styleRankingDTO.setFixedPrice(tuple.get(styleRanking.fixedPrice));
                styleRankingDTO.setMonetaryUnit(tuple.get(styleRanking.monetaryUnit));
                styleRankingDTO.setCategory(new CategoryDTO(tuple.get(categoryStyle.category), mallType));
            }
        }
    }

}
