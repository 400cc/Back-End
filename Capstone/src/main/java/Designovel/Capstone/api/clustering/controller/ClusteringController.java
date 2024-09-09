package Designovel.Capstone.api.clustering.controller;


import Designovel.Capstone.api.clustering.dto.ClusterFilterDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringDTO;
import Designovel.Capstone.api.clustering.service.ClusteringService;
import Designovel.Capstone.domain.mallType.enumType.MallTypeId;
import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "클러스터링", description = "클러스터링 API")
@RequestMapping("/cluster")
public class ClusteringController {

    private final ClusteringService clusteringService;

    @PostMapping(value = "/search")
    public ResponseEntity<List<ClusteringDTO>> getClustering(ClusterFilterDTO clusterFilterDTO) {
        int nClusters = clusterFilterDTO.getNClusters();
        if (nClusters < 3 || nClusters % 2 == 0) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.INVALID_NCLUSTER_NUMBER);
        }
        MallTypeId.checkMallTypeId(clusterFilterDTO.getMallTypeId());
        return clusteringService.processClustering(clusterFilterDTO);
    }
}