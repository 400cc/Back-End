package Designovel.Capstone.domain;

import Designovel.Capstone.entity.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRankingAvgDTO {
    private Product product;
    private String mallType;
    private Double averageDiscountedPrice;
    private Double averageFixedPrice;
    private Double exposureIndex;
    private String monetaryUnit;


}
