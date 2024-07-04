package Designovel.Capstone.api.imageSearch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageSearchService {

    private final WebClient webClient;

    public ResponseEntity<String> sendImageSearchRequest(MultipartFile image, String category, int topNum) throws IOException {
        byte[] imageBytes = image.getBytes();
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image_upload", new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return image.getOriginalFilename();
            }
        });

        body.add("category", category);
        body.add("top_num", topNum);

        Mono<String> response = webClient.post()
                .uri("/process/image")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class);

        String result = response.block();
        return ResponseEntity.ok(result);
    }
}
