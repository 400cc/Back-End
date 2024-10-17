package Designovel.Capstone.api.styleFilter.service;

import Designovel.Capstone.api.styleFilter.dto.DupeExposureIndex;
import Designovel.Capstone.api.styleFilter.dto.StyleBasicDetailDTO;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.image.ImageRepository;
import Designovel.Capstone.domain.style.skuAttribute.SKUAttribute;
import Designovel.Capstone.domain.style.skuAttribute.SKUAttributeRepository;
import Designovel.Capstone.domain.style.style.StyleId;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingRepository;
import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StyleDetailService {

    private final StyleRankingRepository styleRankingRepository;
    private final SKUAttributeRepository skuAttributeRepository;
    private final ImageRepository imageRepository;

    /**
     * 상품의 공통 정보(노출 지수, 카테고리, 가격, SKU, 이미지 등) 반환 메서드
     * 1. 해당 상품의 노출 지수, 가격 및 기타 정보 DB 조회 후 StyleBasicDetailDTO 객체 생성
     * 2. SKU가 있을 시, DB 조회 후 StyleBasicDetailDTO 객체에 SKU 지정
     * 3. 해당 상품의 이미지 DB 조회 후 StyleBasicDetailDTO 객체에 이미지 지정
     * @param styleId
     * @param mallType
     * @return 공통 정보가 담긴 StyleBasicDetailDTO 반환
     */
    public StyleBasicDetailDTO getStyleBasicDetailDTO(String styleId, String mallType) {
        StyleBasicDetailDTO styleBasicDetail = getExposureIndexAndPriceInfo(styleId, mallType);
        setStyleDetailSKUAttribute(styleBasicDetail);
        setStyleDetailImage(styleBasicDetail);
        return styleBasicDetail;
    }

    /**
     * 해당 상품의 노출 지수와 가격 정보 반환 메서드
     * 1. 노출 지수와 카테고리 조회
     * 2. 가격, 기타 공통 정보 조회
     * 3. 중복 카테고리에 따른 여러개의 노출 지수 처리
     * @param styleId
     * @param mallTypeId
     * @return 전체 공통 정보를 styleBasicDetailDTO로 만들어 반환
     */
    public StyleBasicDetailDTO getExposureIndexAndPriceInfo(String styleId, String mallTypeId) {
        //해당 상품의 노출 지수 및 카테고리 조회
        List<Object[]> rankScore = styleRankingRepository.findRankScoreByStyle(styleId, mallTypeId);

        //해당 상품의 가격 및 기타 정보 조회
        Pageable pageable = PageRequest.of(0, 1);
        Page<StyleBasicDetailDTO> styleBasicDetailDTOPage = styleRankingRepository.findPriceInfoByStyle(styleId, mallTypeId, pageable);

        if (styleBasicDetailDTOPage.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.STYLE_DETAIL_IS_EMPTY);
        }

        StyleBasicDetailDTO styleBasicDetailDTO = styleBasicDetailDTOPage.getContent().get(0);

        //중복 카테고리에 따른 중복 노출 지수 처리
        List<DupeExposureIndex> exposureIndexList = rankScore.stream().map(data -> new DupeExposureIndex(styleId, mallTypeId, ((Number) data[1]).floatValue(), (Category) data[0]))
                .collect(Collectors.toList());
        styleBasicDetailDTO.setExposureIndexList(exposureIndexList);
        return styleBasicDetailDTO;
    }

    /**
     * StyleBasicDetailDTO에 해당 상품의 이미지를 조회하여 설정하는 메서드
     * - 해당 상품에 맞는 이미지를 조회하여 설정
     * @param styleBasicDetailDTO
     */
    public void setStyleDetailImage(StyleBasicDetailDTO styleBasicDetailDTO) {
        StyleId styleId = new StyleId(styleBasicDetailDTO.getStyleId(), styleBasicDetailDTO.getMallTypeId());
        List<Image> image = imageRepository.findByStyle_Id(styleId);
        if (!image.isEmpty()) {
            styleBasicDetailDTO.setImageList(image);
        }
    }

    /**
     * StyleBasicDetailDTO에 해당 상품의 SKU를 조회하여 설정하는 메서드
     * - 만약 SKU 값이 있다면 SKU 설정
     * @param styleBasicDetailDTO
     */
    public void setStyleDetailSKUAttribute(StyleBasicDetailDTO styleBasicDetailDTO) {
        List<SKUAttribute> skuAttribute = skuAttributeRepository.findByStyleId(styleBasicDetailDTO.getStyleId(), styleBasicDetailDTO.getMallTypeId());
        if (!skuAttribute.isEmpty()) {
            skuAttribute.forEach(sku -> styleBasicDetailDTO.getSkuAttribute().put(sku.getAttrKey(), sku.getAttrValue()));
        }
    }



}
