package Designovel.Capstone.api.clustering.dto;

import Designovel.Capstone.domain.mallType.MallType;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClusteringDTO {
    private String styleId;
    private String mallTypeId;
    private String imageURL;
    private float x;
    private float y;
    private int cluster;

    public ClusteringDTO(String styleId, MallType mallType) {
        this.styleId = styleId;
        this.mallTypeId = mallType.getMallTypeId();
    }

    public ClusteringDTO(String styleId, float x, float y, String url, int cluster, String mallTypeId) {
        this.styleId = styleId;
        this.x = x;
        this.y = y;
        this.imageURL = url;
        this.cluster = cluster;
        this.mallTypeId = mallTypeId;
    }
}
