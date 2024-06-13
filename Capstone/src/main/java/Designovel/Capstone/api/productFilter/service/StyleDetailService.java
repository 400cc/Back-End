package Designovel.Capstone.api.productFilter.service;

import Designovel.Capstone.api.productFilter.dto.DupeExposureIndex;
import Designovel.Capstone.api.productFilter.dto.StyleBasicDetailDTO;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.image.ImageRepository;
import Designovel.Capstone.domain.product.product.ProductId;
import Designovel.Capstone.domain.product.productRanking.ProductRankingRepository;
import Designovel.Capstone.domain.product.skuAttribute.SKUAttribute;
import Designovel.Capstone.domain.product.skuAttribute.SKUAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StyleDetailService {

    private final ProductRankingRepository productRankingRepository;
    private final SKUAttributeRepository skuAttributeRepository;
    private final ImageRepository imageRepository;

    public void setProductDetailImage(StyleBasicDetailDTO styleBasicDetailDTO) {
        ProductId productId = new ProductId(styleBasicDetailDTO.getProductId(), styleBasicDetailDTO.getMallType());
        List<Image> image = imageRepository.findByProduct_Id(productId);
        if (!image.isEmpty()) {
            styleBasicDetailDTO.setImageList(image);
        }
    }

    public void setProductDetailSKUAttribute(StyleBasicDetailDTO styleBasicDetailDTO) {
        ProductId productId = new ProductId(styleBasicDetailDTO.getProductId(), styleBasicDetailDTO.getMallType());
        List<SKUAttribute> skuAttribute = skuAttributeRepository.findByProduct_Id(productId);
        if (!skuAttribute.isEmpty()) {
            skuAttribute.stream().forEach(sku -> {
                styleBasicDetailDTO.getSkuAttribute().put(sku.getAttrKey(), sku.getAttrValue());
            });
        }
    }

    public StyleBasicDetailDTO getExposureIndexAndPriceInfo(String productId, String mallType) {
        List<Object[]> rankScore = productRankingRepository.findRankScoreByProduct(productId, mallType);
        Pageable pageable = PageRequest.of(0, 1);
        StyleBasicDetailDTO styleBasicDetailDTO = productRankingRepository.findPriceInfoByProduct(productId, mallType, pageable).getContent().get(0);

        List<DupeExposureIndex> exposureIndexList = rankScore.stream().map(data -> new DupeExposureIndex(productId, mallType, ((Number) data[1]).floatValue(), (Category) data[0]))
                .collect(Collectors.toList());
        styleBasicDetailDTO.setExposureIndexList(exposureIndexList);
        return styleBasicDetailDTO;
    }

    public StyleBasicDetailDTO getProductBasicDetailDTO(String productId, String mallType) {
        StyleBasicDetailDTO productBasicDetail = getExposureIndexAndPriceInfo(productId, mallType);
        setProductDetailSKUAttribute(productBasicDetail);
        setProductDetailImage(productBasicDetail);
        return productBasicDetail;
    }
}
