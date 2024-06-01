package Designovel.Capstone.service.product;

import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.service.image.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRankingService productRankingService;
    private final SKUAttributeService skuAttributeService;
    private final ImageService imageService;

    public ProductBasicDetailDTO getProductBasicDetailDTO(String productId, String mallType) {
        ProductBasicDetailDTO productBasicDetail = productRankingService.getExposureIndexAndPriceInfo(productId, mallType);
        skuAttributeService.setProductDetailSKUAttribute(productBasicDetail);
        imageService.setProductDetailImage(productBasicDetail);
        return productBasicDetail;
    }
}
