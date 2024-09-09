package Designovel.Capstone.api.clustering.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClusterFilterDTO {
    private String mallTypeId;
    private List<Integer> categoryList;
    private int nClusters = 3;

}
