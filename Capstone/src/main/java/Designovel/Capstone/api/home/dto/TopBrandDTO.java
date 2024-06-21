package Designovel.Capstone.api.home.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopBrandDTO {
    private String brand;
    private Float exposureIndexSum;
    private String mallTypeId;
}
