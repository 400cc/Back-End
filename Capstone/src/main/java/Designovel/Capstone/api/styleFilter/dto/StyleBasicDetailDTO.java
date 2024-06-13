package Designovel.Capstone.api.styleFilter.dto;

import Designovel.Capstone.domain.image.Image;
import Designovel.Capstone.domain.style.style.StyleId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StyleBasicDetailDTO {

    private String styleId;
    private String mallType;
    private String brand;
    private Integer discountedPrice;
    private Integer fixedPrice;
    private String monetaryUnit;
    private LocalDate crawledDate;
    private List<Image> imageList = new ArrayList<>();
    private Map<String, Object> skuAttribute = new HashMap<>();
    private List<DupeExposureIndex> exposureIndexList = new ArrayList<>();

    public StyleBasicDetailDTO(String brand, int discountedPrice, int fixedPrice, String monetaryUnit, LocalDate crawledDate, StyleId styleId) {
        this.brand = brand;
        this.discountedPrice = discountedPrice;
        this.fixedPrice = fixedPrice;
        this.monetaryUnit = monetaryUnit;
        this.crawledDate = crawledDate;
        this.styleId = styleId.getStyleId();
        this.mallType = styleId.getMallTypeId();
    }

}
