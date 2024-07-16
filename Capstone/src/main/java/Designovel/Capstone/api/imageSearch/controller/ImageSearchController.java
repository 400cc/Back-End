package Designovel.Capstone.api.imageSearch.controller;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import Designovel.Capstone.api.imageSearch.service.ImageSearchService;
import Designovel.Capstone.domain.mallType.enumType.MallTypeId;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "이미지 검색", description = "이미지 검색 API")
@RequestMapping("/image")
public class ImageSearchController {

    private final ImageSearchService imageSearchService;

    @PostMapping("/search")
    public ResponseEntity<String> getSimilarImages(@ModelAttribute ImageSearchDTO imageSearchDTO) {
        MallTypeId.checkMallTypeId(imageSearchDTO.getMallTypeId());
        return imageSearchService.sendImageSearchRequest(imageSearchDTO);
    }
}
