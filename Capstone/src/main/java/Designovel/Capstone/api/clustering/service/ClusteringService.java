package Designovel.Capstone.api.clustering.service;

import Designovel.Capstone.api.clustering.dto.ClusterFilterDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringDTO;
import Designovel.Capstone.api.clustering.dto.ClusteringStyleDTO;
import Designovel.Capstone.api.clustering.queryDSL.ClusteringQueryDSL;
import Designovel.Capstone.domain.category.category.CategoryDTO;
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
        Map<String, Object> requestBody = buildClusteringRequestBody(clusterFilterDTO);
        List<Map<String, Object>> response = sendClusteringRequest(requestBody);
        List<ClusteringDTO> clusteringDTOList = buildClusteringDTOResponse(response, clusterFilterDTO.getMallTypeId());
        return ResponseEntity.ok(clusteringDTOList);
    }

    private List<Map<String, Object>> sendClusteringRequest(Map<String, Object> requestBody) {

        return webClient.post()
                .uri("/clustering")
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {
                })
                .map(response -> (List<Map<String, Object>>) response.get("data_points"))
                .onErrorMap(WebClientResponseException.class, e -> {
                    log.info(e.getMessage());
                    return new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.FASTAPI_SERVER_ERROR);
                })
                .block();

    }


    public List<ClusteringDTO> buildClusteringDTOResponse(List<Map<String, Object>> fastAPIResponse, String mallTypeId) {
        List<ClusteringDTO> response = new ArrayList<>();
        for (Map<String, Object> dataPoint : fastAPIResponse) {
            if (((Number) dataPoint.get("x")).floatValue() != 0 && ((Number) dataPoint.get("y")).floatValue() != 0) {
                ClusteringDTO dto = ClusteringDTO.builder()
                        .styleId(dataPoint.get("style_id").toString())
                        .x(((Number) dataPoint.get("x")).floatValue())
                        .y(((Number) dataPoint.get("y")).floatValue())
                        .imageURL(dataPoint.get("url").toString())
                        .cluster((int) dataPoint.get("cluster"))
                        .mallTypeId(mallTypeId)
                        .build();
                response.add(dto);
            }
        }
        return response;
    }


    private Map<String, Object> buildClusteringRequestBody(ClusterFilterDTO clusterFilterDTO) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("mall_type_id", clusterFilterDTO.getMallTypeId());
        requestBody.put("category_list", clusterFilterDTO.getCategoryList());
        requestBody.put("n_clusters", clusterFilterDTO.getNClusters());
        return requestBody;
    }


    public ResponseEntity<Object> getStyleInfo(String mallTypeId, String styleId) {
        ClusteringStyleDTO clusteringStyleDTO = Optional.ofNullable(clusteringQueryDSL.findStyleInfoById(mallTypeId, styleId))
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.STYLE_DETAIL_IS_EMPTY));
        List<CategoryDTO> categoryListByStyle = clusteringQueryDSL.findCategoryListByStyle(styleId);
        clusteringStyleDTO.setCategoryList(categoryListByStyle);
        return ResponseEntity.ok(clusteringStyleDTO);
    }
}
