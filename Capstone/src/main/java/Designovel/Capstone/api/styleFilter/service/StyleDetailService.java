package Designovel.Capstone.api.styleFilter.service;

import Designovel.Capstone.api.styleFilter.dto.DupeExposureIndex;
import Designovel.Capstone.api.styleFilter.dto.StyleBasicDetailDTO;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.image.ImageRepository;
import Designovel.Capstone.domain.style.style.StyleId;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingRepository;
import Designovel.Capstone.domain.style.skuAttribute.SKUAttribute;
import Designovel.Capstone.domain.style.skuAttribute.SKUAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StyleDetailService {

    private final StyleRankingRepository styleRankingRepository;
    private final SKUAttributeRepository skuAttributeRepository;
    private final ImageRepository imageRepository;

    public void setStyleDetailImage(StyleBasicDetailDTO styleBasicDetailDTO) {
        StyleId styleId = new StyleId(styleBasicDetailDTO.getStyleId(), styleBasicDetailDTO.getMallTypeId());
        List<Image> image = imageRepository.findByStyle_Id(styleId);
        if (!image.isEmpty()) {
            styleBasicDetailDTO.setImageList(image);
        }
    }

    public void setStyleDetailSKUAttribute(StyleBasicDetailDTO styleBasicDetailDTO) {
        List<SKUAttribute> skuAttribute = skuAttributeRepository.findByStyleId(styleBasicDetailDTO.getStyleId(), styleBasicDetailDTO.getMallTypeId());
        if (!skuAttribute.isEmpty()) {
            skuAttribute.stream().forEach(sku -> {
                styleBasicDetailDTO.getSkuAttribute().put(sku.getAttrKey(), sku.getAttrValue());
            });
        }
    }

    public StyleBasicDetailDTO getExposureIndexAndPriceInfo(String styleId, String mallType) {
        List<Object[]> rankScore = styleRankingRepository.findRankScoreByStyle(styleId, mallType);
        Pageable pageable = PageRequest.of(0, 1);
        StyleBasicDetailDTO styleBasicDetailDTO = styleRankingRepository.findPriceInfoByStyle(styleId, mallType, pageable).getContent().get(0);

        List<DupeExposureIndex> exposureIndexList = rankScore.stream().map(data -> new DupeExposureIndex(styleId, mallType, ((Number) data[1]).floatValue(), (Category) data[0]))
                .collect(Collectors.toList());
        styleBasicDetailDTO.setExposureIndexList(exposureIndexList);
        return styleBasicDetailDTO;
    }

    public StyleBasicDetailDTO getStyleBasicDetailDTO(String styleId, String mallType) {
        StyleBasicDetailDTO styleBasicDetail = getExposureIndexAndPriceInfo(styleId, mallType);
        setStyleDetailSKUAttribute(styleBasicDetail);
        setStyleDetailImage(styleBasicDetail);
        return styleBasicDetail;
    }
}
