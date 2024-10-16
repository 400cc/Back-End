package Designovel.Capstone.api.clustering.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClusterFilterDTO {
    private String mallTypeId;
    private List<Integer> categoryList;
    private Integer nClusters;
}
