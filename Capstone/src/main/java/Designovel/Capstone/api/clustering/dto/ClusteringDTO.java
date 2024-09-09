package Designovel.Capstone.api.clustering.dto;

import Designovel.Capstone.domain.mallType.MallType;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClusteringDTO {
    private String styleId;
    private String styleName;
    private MallType mallType;
    private String brand;
    private String imageURL;
    private float x;
    private float y;
    private int cluster;

    public ClusteringDTO(String styleId, String styleName, MallType mallType, String brand) {
        this.styleId = styleId;
        this.styleName = styleName;
        this.mallType = mallType;
        this.brand = brand;
    }

}
