package Designovel.Capstone.domain;

import Designovel.Capstone.entity.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProductDetailDTO {

    private String productId;
    private String mallType;
    private String brand;
    private Integer discountedPrice;
    private Integer fixedPrice;
    private String monetaryUnit;
    private Date crawledDate;
    private List<Image> imageList = new ArrayList<>();
    private List<DupeExposureIndex> dupeExposureIndexList = new ArrayList<>();

    public ProductDetailDTO(String brand, int discountedPrice, int fixedPrice, String monetaryUnit, Date crawledDate) {
        this.brand = brand;
        this.discountedPrice = discountedPrice;
        this.fixedPrice = fixedPrice;
        this.monetaryUnit = monetaryUnit;
        this.crawledDate = crawledDate;
    }

}
