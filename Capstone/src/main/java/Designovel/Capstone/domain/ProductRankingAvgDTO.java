package Designovel.Capstone.domain;

import Designovel.Capstone.entity.CategoryClosure;
import Designovel.Capstone.entity.CategoryProduct;
import Designovel.Capstone.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRankingAvgDTO {
    private String productId;
    private String brand;
    private String mallType;
    private CategoryProduct categoryProduct;
    private Double averageDiscountedPrice;
    private Double averageFixedPrice;
    private Double exposureIndex;
    private String monetaryUnit;
    private Image images;
}
