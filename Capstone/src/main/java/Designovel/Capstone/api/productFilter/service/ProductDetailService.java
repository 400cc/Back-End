package Designovel.Capstone.api.productFilter.service;

import Designovel.Capstone.api.productFilter.dto.DupeExposureIndex;
import Designovel.Capstone.api.productFilter.dto.ProductBasicDetailDTO;
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
public class ProductDetailService {

    private final ProductRankingRepository productRankingRepository;
    private final SKUAttributeRepository skuAttributeRepository;
    private final ImageRepository imageRepository;

    public void setProductDetailImage(ProductBasicDetailDTO productBasicDetailDTO) {
        ProductId productId = new ProductId(productBasicDetailDTO.getProductId(), productBasicDetailDTO.getMallType());
        List<Image> image = imageRepository.findByProduct_Id(productId);
        if (!image.isEmpty()) {
            productBasicDetailDTO.setImageList(image);
        }
    }

    public void setProductDetailSKUAttribute(ProductBasicDetailDTO productBasicDetailDTO) {
        ProductId productId = new ProductId(productBasicDetailDTO.getProductId(), productBasicDetailDTO.getMallType());
        List<SKUAttribute> skuAttribute = skuAttributeRepository.findByProduct_Id(productId);
        if (!skuAttribute.isEmpty()) {
            skuAttribute.stream().forEach(sku -> {
                productBasicDetailDTO.getSkuAttribute().put(sku.getAttrKey(), sku.getAttrValue());
            });
        }
    }

    public ProductBasicDetailDTO getExposureIndexAndPriceInfo(String productId, String mallType) {
        List<Object[]> rankScore = productRankingRepository.findRankScoreByProduct(productId, mallType);
        Pageable pageable = PageRequest.of(0, 1);
        ProductBasicDetailDTO productBasicDetailDTO = productRankingRepository.findPriceInfoByProduct(productId, mallType, pageable).getContent().get(0);

        List<DupeExposureIndex> exposureIndexList = rankScore.stream().map(data -> new DupeExposureIndex(productId, mallType, ((Number) data[1]).floatValue(), (Category) data[0]))
                .collect(Collectors.toList());
        productBasicDetailDTO.setExposureIndexList(exposureIndexList);
        return productBasicDetailDTO;
    }

    public ProductBasicDetailDTO getProductBasicDetailDTO(String productId, String mallType) {
        ProductBasicDetailDTO productBasicDetail = getExposureIndexAndPriceInfo(productId, mallType);
        setProductDetailSKUAttribute(productBasicDetail);
        setProductDetailImage(productBasicDetail);
        return productBasicDetail;
    }
}
