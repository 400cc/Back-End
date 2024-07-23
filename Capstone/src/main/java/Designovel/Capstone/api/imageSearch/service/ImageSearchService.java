package Designovel.Capstone.api.imageSearch.service;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import Designovel.Capstone.api.imageSearch.queryDSL.ImageSearchQueryDSL;
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
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageSearchService {

    private final WebClient webClient;
    private final ImageSearchQueryDSL imageSearchQueryDSL;
    private final CategoryService categoryService;

    public ResponseEntity<String> sendImageSearchRequest(ImageSearchDTO imageSearchDTO) {
        List<String> styleByCategory = Collections.emptyList();
        String categoryName = "apparel";
        if (!imageSearchDTO.getCategoryList().isEmpty() && imageSearchDTO.getMallTypeId() != null) {
            styleByCategory = imageSearchQueryDSL.findStyleByCategory(imageSearchDTO);
            categoryName = categoryService.findNameByCategoryIdList(imageSearchDTO.getCategoryList());
        }

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image_upload", imageSearchDTO.getImage().getResource());
        bodyBuilder.part("category", categoryName);
        bodyBuilder.part("mall_type_id", imageSearchDTO.getMallTypeId());
        bodyBuilder.part("offset", imageSearchDTO.getOffset());
        bodyBuilder.part("style_id_list", styleByCategory);


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
}
