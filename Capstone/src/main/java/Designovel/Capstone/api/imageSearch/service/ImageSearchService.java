package Designovel.Capstone.api.imageSearch.service;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
@Slf4j
@RequiredArgsConstructor
public class ImageSearchService {

    //GPU 서버에서 이미지 누끼를 위한 기본값 apparel로 초기화
    private static final String DEFAULT_CATEGORY_NAME = "apparel";
    private final WebClient webClient;

    /**
     * 이미지 검색을 처리하는 메서드
     * 1. categoryName: 이미지 검색에서 누끼를 따기 위한 프롬프트 생성(값이 있을 경우 ", "로 하나의 스트링으로 만듦)
     * 2. 요청을 보내고 응답 반환
     * @param imageSearchDTO
     * @return
     */
    public ResponseEntity<String> processImageSearch(ImageSearchDTO imageSearchDTO) {
        String categoryName = DEFAULT_CATEGORY_NAME;

        if (!imageSearchDTO.getCategoryNameList().isEmpty()) {
            categoryName = imageSearchDTO.getCategoryNameList();
        }

        log.info("ImageSearchCategoryName: {}", categoryName);
        log.info("ImageSearchCategoryList: {}", imageSearchDTO.getCategoryList());

        MultipartBodyBuilder request = buildImageSearchRequestBody(imageSearchDTO, categoryName);
        return sendImageSearchRequest(request);
    }


    /**
     * GPU 서버에 이미지 검색 요청을 보내고 응답을 받는 메서드
     * @param bodyBuilder
     * @return 요청 성공 시 200, 에러 발생 시 GPU 서버의 에러 전송
     */
    private ResponseEntity<String> sendImageSearchRequest(MultipartBodyBuilder bodyBuilder) {
        try {
            String response = webClient.post()
                    .uri("/process/image")
                    .contentType(MediaType.MULTIPART_FORM_DATA)
                    .body(BodyInserters.fromMultipartData(bodyBuilder.build()))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return ResponseEntity.ok(response);
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getMessage());
        }
    }

    /**
     * 이미지 검색 요청 Body 생성 메서드
     * @param imageSearchDTO
     * @param categoryName
     * @return
     */
    private MultipartBodyBuilder buildImageSearchRequestBody(ImageSearchDTO imageSearchDTO, String categoryName) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image_upload", imageSearchDTO.getImage().getResource());
        bodyBuilder.part("category_name", categoryName);
        bodyBuilder.part("category_id_list", imageSearchDTO.getCategoryList());
        bodyBuilder.part("mall_type_id", imageSearchDTO.getMallTypeId());
        bodyBuilder.part("offset", imageSearchDTO.getOffset());
        return bodyBuilder;
    }
}
