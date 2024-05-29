package Designovel.Capstone.service.product;

import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.entity.SKUAttribute;
import Designovel.Capstone.entity.id.ProductId;
import Designovel.Capstone.repository.product.SKUAttributeRepository;
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
