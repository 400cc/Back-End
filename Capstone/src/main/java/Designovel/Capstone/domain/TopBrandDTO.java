package Designovel.Capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TopBrandDTO {
    private String brand;
    private Double exposureIndexSum;
    private String mallType;
}
