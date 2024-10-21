package Designovel.Capstone.api.home.controller;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;
import Designovel.Capstone.api.home.dto.TopBrandDTO;
import Designovel.Capstone.api.home.service.NewPriceRangeService;
import Designovel.Capstone.api.home.service.PriceRangeService;
import Designovel.Capstone.api.home.service.TopBrandService;
import Designovel.Capstone.domain.mallType.enumType.MallTypeId;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "메인 화면", description = "메인 화면(통계) API")
@RequestMapping("/home")
public class HomeController {
    private final TopBrandService topBrandService;
    private final PriceRangeService priceRangeService;
    private final NewPriceRangeService newPriceRangeService;

    @Operation(summary = "쇼핑몰 별 Top 10 브랜드 조회", description = "노출 지수 기준으로 Top 10 브랜드 반환",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Json 형식으로 반환 - key: top10BrandList",
                            content = @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = TopBrandDTO.class)))
            })
    @GetMapping("/brand")
    public ResponseEntity<Map<String, List<TopBrandDTO>>> getTop10Brands(@ModelAttribute HomeFilterDTO filterDTO) {
        MallTypeId.checkMallTypeId(filterDTO.getMallTypeId());
        List<TopBrandDTO> top10BrandList = topBrandService.getTop10BrandsByMallTypeId(filterDTO);
        return ResponseEntity.ok(Collections.singletonMap("top10BrandList", top10BrandList));
    }

    @Operation(summary = "쇼핑몰 별 가격대 상품 수 조회", description = "해당 쇼핑몰의 가격대별 상품 수 반환")
    @GetMapping("/price")
    public ResponseEntity<Map<String, Integer>> getPriceRangesCountList(@ModelAttribute HomeFilterDTO filterDTO) {
        MallTypeId.checkMallTypeId(filterDTO.getMallTypeId());
        Map<String, Integer> styleByPriceRange = newPriceRangeService.getPriceRangesCountList(filterDTO);
        return ResponseEntity.ok(styleByPriceRange);
    }

}
