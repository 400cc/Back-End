package Designovel.Capstone.api.home.dto;

import Designovel.Capstone.domain.category.categoryStyle.CategoryStyle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRangeDTO {

    private Integer rankId;
    private CategoryStyle categoryStyle;

    private String brand;

    private float rankScore;

    private String styleName;

    private Integer fixedPrice;

    private Integer discountedPrice;

    private String monetaryUnit;

    private LocalDate crawledDate;
}
