package Designovel.Capstone.api.imageSearch.service;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import Designovel.Capstone.domain.category.category.CategoryService;
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

    private static final String DEFAULT_CATEGORY_NAME = "apparel";
    private final WebClient webClient;

    public ResponseEntity<String> processImageSearch(ImageSearchDTO imageSearchDTO) {
        String categoryName = DEFAULT_CATEGORY_NAME;

        if (!imageSearchDTO.getCategoryNameList().isEmpty()) {
            categoryName = imageSearchDTO.getCategoryNameList();
        }

        log.info("ImageSearchCategoryName: {}", categoryName);

        MultipartBodyBuilder request = buildImageSearchRequestBody(imageSearchDTO, categoryName);
        return sendImageSearchRequest(request);
    }


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
