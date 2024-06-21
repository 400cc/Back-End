package Designovel.Capstone.api.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PriceRangeFilterDTO {
    private List<Integer> category;
    private List<String> brand;
    private String mallTypeId;
}
