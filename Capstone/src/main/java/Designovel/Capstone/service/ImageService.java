package Designovel.Capstone.service;

import Designovel.Capstone.entity.Product;
import com.querydsl.core.Tuple;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static Designovel.Capstone.entity.QProductRanking.productRanking;

@Service
public class ImageService {

//    public void setFirstSequenceImage(List<Tuple> results) {
//        for (Tuple result : results) {
//            Product product = result.get(productRanking.product);
//            if (product != null) {
//                product.setImages(
//                        product.getImages().stream()
//                                .filter(img -> img.getSequence() == 0)
//                                .collect(Collectors.toList())
//                );
//            }
//        }
//    }
}
