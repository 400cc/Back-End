package Designovel.Capstone.api.imageSearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageSearchService {

    private final WebClient webClient;

    public ResponseEntity<String> sendImageSearchRequest(MultipartFile image, String category, int offset) throws IOException {
        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("image_upload", image.getResource());
        bodyBuilder.part("category", category);
        bodyBuilder.part("top_num", offset);

        Mono<String> response = webClient.post()
                .uri("/process/image")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(BodyInserters.fromMultipartData(bodyBuilder.build()))
                .retrieve()
                .bodyToMono(String.class);

        String result = response.block();
        return ResponseEntity.ok(result);
    }
}
