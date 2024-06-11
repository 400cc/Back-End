package Designovel.Capstone.domain.image;

import Designovel.Capstone.domain.product.product.ProductId;
import Designovel.Capstone.api.productFilter.dto.ProductBasicDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    public void setProductDetailImage(ProductBasicDetailDTO productBasicDetailDTO) {
        ProductId productId = new ProductId(productBasicDetailDTO.getProductId(), productBasicDetailDTO.getMallType());
        List<Image> image = imageRepository.findByProduct_Id(productId);
        if (!image.isEmpty()) {
            productBasicDetailDTO.setImageList(image);
        }
    }
}
