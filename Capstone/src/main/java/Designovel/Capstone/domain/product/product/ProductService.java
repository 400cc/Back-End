package Designovel.Capstone.domain.product.product;

import Designovel.Capstone.domain.image.ImageService;
import Designovel.Capstone.domain.product.productRanking.ProductRankingService;
import Designovel.Capstone.api.productFilter.dto.ProductBasicDetailDTO;
import Designovel.Capstone.domain.product.skuAttribute.SKUAttributeService;
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
