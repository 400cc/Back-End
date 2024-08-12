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
public class StyleBasicDetailDTO {

    private String styleId;
    private String mallTypeId;
    private String brand;
    private Integer discountedPrice;
    private Integer fixedPrice;
    private String styleName;
    private String monetaryUnit;
    private LocalDate crawledDate;
    private List<Image> imageList = new ArrayList<>();
    private Map<String, Object> skuAttribute = new HashMap<>();
    private List<DupeExposureIndex> exposureIndexList = new ArrayList<>();

    public StyleBasicDetailDTO(String brand, int discountedPrice, int fixedPrice, String monetaryUnit, LocalDate crawledDate, String styleId, String mallTypeId, String styleName) {
        this.brand = brand;
        this.styleName = styleName;
        this.discountedPrice = discountedPrice;
        this.fixedPrice = fixedPrice;
        this.monetaryUnit = monetaryUnit;
        this.crawledDate = crawledDate;
        this.styleId = styleId;
        this.mallTypeId = mallTypeId;
    }

}
