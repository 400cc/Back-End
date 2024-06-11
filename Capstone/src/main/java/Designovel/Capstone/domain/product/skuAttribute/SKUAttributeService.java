package Designovel.Capstone.domain.product.skuAttribute;

import Designovel.Capstone.domain.product.product.ProductId;
import Designovel.Capstone.api.productFilter.dto.ProductBasicDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SKUAttributeService {
    private final SKUAttributeRepository skuAttributeRepository;

    public void setProductDetailSKUAttribute(ProductBasicDetailDTO productBasicDetailDTO) {
        ProductId productId = new ProductId(productBasicDetailDTO.getProductId(), productBasicDetailDTO.getMallType());
        List<SKUAttribute> skuAttribute = skuAttributeRepository.findByProduct_Id(productId);
        if (!skuAttribute.isEmpty()) {
            skuAttribute.stream().forEach(sku -> {
                productBasicDetailDTO.getSkuAttribute().put(sku.getAttrKey(), sku.getAttrValue());
            });
        }
    }

}
