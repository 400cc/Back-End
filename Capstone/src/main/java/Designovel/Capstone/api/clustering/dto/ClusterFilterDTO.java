package Designovel.Capstone.api.clustering.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterFilterDTO {
    private String mallTypeId;
    private List<Integer> categoryList;

    @JsonProperty("nClusters")
    private Integer nClusters = 3;
}
