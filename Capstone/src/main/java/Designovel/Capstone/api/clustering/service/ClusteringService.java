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

    /**
     * 클러스터링 처리 메서드
     * 1. GPU 서버 요청 생성
     * 2. 요청 후 응답 받음
     * 3. 응답을 클라이언트에게 반환
     * @param clusterFilterDTO
     * @return 예외 발생 시 각 과정 메서드에서 400 에러와 메세지를 함께 발송하므로 200 반환
     */
    public ResponseEntity<List<ClusteringDTO>> processClustering(ClusterFilterDTO clusterFilterDTO) {
        Map<String, Object> requestBody = buildClusteringRequestBody(clusterFilterDTO);
        List<Map<String, Object>> response = sendClusteringRequest(requestBody);
        List<ClusteringDTO> clusteringDTOList = buildClusteringDTOResponse(response, clusterFilterDTO.getMallTypeId());
        return ResponseEntity.ok(clusteringDTOList);
    }

    /**
     * GPU 서버로 클러스터링 요청 전송 메서드
     * @param requestBody
     * @return 클러스터링 서버 예외 발생 시 400 에러와 에러 메세지 전송
     */
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


    /**
     * GPU 서버에서 받은 응답을 클라이언트 형식에 맞게 변환하는 메서드
     * @param fastAPIResponse
     * @param mallTypeId
     * @return
     */
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


    /**
     * GPU 서버 요청 Body 생성 메서드
     * @param clusterFilterDTO
     * @return
     */
    private Map<String, Object> buildClusteringRequestBody(ClusterFilterDTO clusterFilterDTO) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("mall_type_id", clusterFilterDTO.getMallTypeId());
        requestBody.put("category_list", clusterFilterDTO.getCategoryList());
        requestBody.put("n_clusters", clusterFilterDTO.getNClusters());
        return requestBody;
    }


    /**
     * 클러스터링 결과에서 호버 시 해당 상품 정보 반환 메서드
     * @param mallTypeId
     * @param styleId
     * @return 해당 상품이 존재 하지 않을 시 400 에러와 에러 메세지 전송
     */
    public ResponseEntity<Object> getStyleInfo(String mallTypeId, String styleId) {
        ClusteringStyleDTO clusteringStyleDTO = Optional.ofNullable(clusteringQueryDSL.findStyleInfoById(mallTypeId, styleId))
                .orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.STYLE_DETAIL_IS_EMPTY));
        List<CategoryDTO> categoryListByStyle = clusteringQueryDSL.findCategoryListByStyle(styleId);
        clusteringStyleDTO.setCategoryList(categoryListByStyle);
        return ResponseEntity.ok(clusteringStyleDTO);
    }
}
