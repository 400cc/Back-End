package Designovel.Capstone.service.image;

import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.entity.Image;
import Designovel.Capstone.entity.id.ProductId;
import Designovel.Capstone.repository.image.ImageRepository;
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
