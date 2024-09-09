package Designovel.Capstone.api.clustering.service;

import Designovel.Capstone.api.clustering.dto.ClusterFilterDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringDTO;
import Designovel.Capstone.api.clustering.queryDSL.ClusteringQueryDSL;
import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class ClusteringService {
    private final ClusteringQueryDSL clusteringQueryDSL;
    private final WebClient webClient;

    public ResponseEntity<List<ClusteringDTO>> processClustering(ClusterFilterDTO clusterFilterDTO) {
        Map<String, ClusteringDTO> clusteringDTOMap = clusteringQueryDSL.findStyleInfoByCategory(clusterFilterDTO);

//        Map<String, Object> requestBody = buildClusteringRequestBody(clusteringDTOMap, clusterFilterDTO.getNClusters());
//        List<Map<String, Object>> response = sendClusteringRequest(requestBody);
//        updateClusteringDTOList(clusteringDTOMap, response);
        return ResponseEntity.ok(new ArrayList<>(clusteringDTOMap.values()));
    }

    private List<Map<String, Object>> sendClusteringRequest(Map<String, Object> requestBody) {
        try {
            List<Map<String, Object>> response = webClient.post()
                    .uri("/clustering")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(requestBody))
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<List<Map<String, Object>>>() {})
                    .block();
            return response;
        } catch (WebClientResponseException e) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.FASTAPI_SERVER_ERROR);
        }
    }


    public void updateClusteringDTOList(Map<String, ClusteringDTO> clusteringDTOMap, List<Map<String, Object>> response) {
        for (Map<String, Object> dataPoint : response) {
            String styleId = (String) dataPoint.get("style_id");
            float x = ((Number) dataPoint.get("x")).floatValue();
            float y = ((Number) dataPoint.get("y")).floatValue();
            String url = dataPoint.get("url").toString();
            int cluster = (int) dataPoint.get("cluster");

            ClusteringDTO dto = clusteringDTOMap.get(styleId);
            if (dto != null) {
                dto.setX(x);
                dto.setY(y);
                dto.setImageURL(url);
                dto.setCluster(cluster);
            }
        }
    }




    private Map<String, Object> buildClusteringRequestBody(Map<String, ClusteringDTO> clusteringDTOList, int nClusters) {
        Map<String, Object> requestBody = new HashMap<>();
        List<String> styleIdList = clusteringDTOList.values().stream().map(ClusteringDTO::getStyleId).toList();
        requestBody.put("style_id_list", styleIdList);
        requestBody.put("n_clusters", nClusters);
        return requestBody;
    }


}
