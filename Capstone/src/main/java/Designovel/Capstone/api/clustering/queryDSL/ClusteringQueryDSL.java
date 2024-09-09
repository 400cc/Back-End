package Designovel.Capstone.api.clustering.queryDSL;

import Designovel.Capstone.api.clustering.dto.ClusterFilterDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringDTO;
import com.querydsl.core.BooleanBuilder;

import java.util.Map;

public interface ClusteringQueryDSL {
    Map<String, ClusteringDTO> findStyleInfoByCategory(ClusterFilterDTO filterDTO);
//    BooleanBuilder buildClusterFilter(ClusterFilterDTO filterDTO);
}

