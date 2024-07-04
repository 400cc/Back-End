package Designovel.Capstone.api.imageSearch.controller;

import Designovel.Capstone.api.imageSearch.service.ImageSearchService;
import Designovel.Capstone.domain.image.Image;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "이미지 검색", description = "이미지 검색 API")
@RequestMapping("/image")
public class ImageSearchController {

    private final ImageSearchService imageSearchService;
    @GetMapping("/search")
    public ResponseEntity<String> getSimilarImages(@RequestParam MultipartFile image,
                                              @RequestParam String category,
                                              @RequestParam int topNum) throws IOException {
        return imageSearchService.sendImageSearchRequest(image, category, topNum);
    }
}
