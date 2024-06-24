package Designovel.Capstone.api.styleFilter.controller;

import Designovel.Capstone.api.styleFilter.dto.CategoryNode;
import Designovel.Capstone.api.styleFilter.dto.StyleFilterDTO;
import Designovel.Capstone.api.styleFilter.service.CategoryNodeService;
import Designovel.Capstone.api.styleFilter.service.StyleFilterService;
import Designovel.Capstone.domain.mallType.enumType.MallTypeId;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingDTO;
import Designovel.Capstone.domain.style.styleRanking.StyleRankingService;
import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "상품 필터", description = "상품 필터 조회 API")
@RequestMapping("/style/filter")
public class StyleFilterController {

    private final StyleFilterService styleFilterService;
    private final CategoryNodeService categoryNodeService;
    private final StyleRankingService styleRankingService;


    @Operation(summary = "전체 스타일 필더 조회", description = "필터를 적용하여 스타일의 기본 정보(가격, 노출 지수 등 조회)")
    @GetMapping
    public ResponseEntity<Page<StyleRankingDTO>> getStyleRanking(@ModelAttribute StyleFilterDTO filter, Optional<Integer> page) {
        Integer pageNumber = page.orElseThrow(() -> new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.PAGE_NUM_IS_NULL));
        Page<StyleRankingDTO> styleRankings = styleFilterService.getStyleRankingByFilter(filter, pageNumber);
        return ResponseEntity.ok(styleRankings);
    }

    @Operation(summary = "특정 쇼핑몰의 카테고리 계층도 조회", description = "특정 쇼핑몰의 카테고리 트리 조회")
    @ApiResponse(responseCode = "200", description = "카테고리 계층 조회", content = @Content(schema = @Schema(implementation = CategoryNode.class)))
    @GetMapping("/category/{mallTypeId}")
    public ResponseEntity<List<CategoryNode>> getCategories(@PathVariable("mallTypeId") String mallTypeId) {
        MallTypeId.checkMallTypeId(mallTypeId);
        return ResponseEntity.ok(categoryNodeService.getCategoryTree(mallTypeId));
    }


    @Operation(summary = "특정 쇼핑몰의 브랜드 목록 조회", description = "특정 쇼핑몰의 브랜드 목록을 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리스트에 해당 쇼핑몰 brand를 담아서 전송",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(name = "Json 리스트", value = "{ \"brand\": [\"브랜드1\", \"브랜드2\"] }")
                    ))
    })
    @GetMapping("/brand/{mallTypeId}")
    public ResponseEntity<Object> getBrandsByMallTypeId(@PathVariable("mallTypeId") String mallTypeId) {
        MallTypeId.checkMallTypeId(mallTypeId);
        Map<String, List<String>> distinctBrandMap = new HashMap<>();
        distinctBrandMap.put("brand", styleRankingService.getBrandsByMallTypeId(mallTypeId));
        return ResponseEntity.ok(distinctBrandMap);
    }


}
