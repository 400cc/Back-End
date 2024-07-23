package Designovel.Capstone.api.imageSearch.service;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import Designovel.Capstone.api.imageSearch.queryDSL.ImageSearchQueryDSL;
import Designovel.Capstone.domain.category.category.CategoryService;
import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageSearchService {

    private final WebClient webClient;
    private final ImageSearchQueryDSL imageSearchQueryDSL;
    private final CategoryService categoryService;
    private static final String DEFAULT_CATEGORY_NAME = "apparel";

    public ResponseEntity<String> processImageSearch(ImageSearchDTO imageSearchDTO) {
        List<String> styleByCategory = Collections.emptyList();
        String categoryName = DEFAULT_CATEGORY_NAME;

        if (!imageSearchDTO.getCategoryList().isEmpty() && imageSearchDTO.getMallTypeId() != null) {
            categoryName = categoryService.findNameByCategoryIdList(imageSearchDTO.getCategoryList());
            styleByCategory = imageSearchQueryDSL.findStyleByCategory(imageSearchDTO);

            if (styleByCategory.isEmpty()) {
                throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.STYLE_IMAGE_DOESNT_EXIST_FOR_CATEGORY);
            }
        }

        MultipartBodyBuilder request = buildImageSearchRequestBody(imageSearchDTO, categoryName, styleByCategory);
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

    private MultipartBodyBuilder buildImageSearchRequestBody(ImageSearchDTO imageSearchDTO, String categoryName, List<String> styleByCategory) {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image_upload", imageSearchDTO.getImage().getResource());
        bodyBuilder.part("category", categoryName);
        bodyBuilder.part("mall_type_id", imageSearchDTO.getMallTypeId());
        bodyBuilder.part("offset", imageSearchDTO.getOffset());
        bodyBuilder.part("style_id_list", styleByCategory);
        return bodyBuilder;
    }
}
