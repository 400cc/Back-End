package Designovel.Capstone.api.imageSearch.controller;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import Designovel.Capstone.api.imageSearch.service.ImageSearchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "이미지 검색", description = "이미지 검색 API")
@RequestMapping("/image")
public class ImageSearchController {

    private final ImageSearchService imageSearchService;

    @PostMapping(value = "/search", consumes = "multipart/form-data")
    public ResponseEntity<String> getSimilarImages(
            @RequestPart("image") MultipartFile image,
            @RequestParam("mallTypeId") String mallTypeId,
            @RequestParam("categoryList") String categoryListStr,
            @RequestParam("categoryNameList") String categoryNameListStr,
            @RequestParam(value = "offset", defaultValue = "5") int offset) {

        ImageSearchDTO imageSearchDTO = new ImageSearchDTO(image, categoryListStr, mallTypeId, offset, categoryNameListStr);
        return imageSearchService.processImageSearch(imageSearchDTO);
    }
}
