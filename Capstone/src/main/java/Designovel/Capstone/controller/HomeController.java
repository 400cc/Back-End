package Designovel.Capstone.controller;

import Designovel.Capstone.domain.TopBrandDTO;
import Designovel.Capstone.service.product.ProductRankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "메인 화면", description = "메인 화면(통계) API")
@RequestMapping("/home")
public class HomeController {
    private final ProductRankingService productRankingService;

    @Operation(summary = "쇼핑몰 별 Top 10 브랜드 조회", description = "노출 지수 기준으로 Top 10 브랜드 반환")
    @GetMapping("/brand/{mallType}")
    public ResponseEntity<Object> getTop10Brands(@PathVariable("mallType") String mallType) {
        List<TopBrandDTO> top10BrandList = productRankingService.getTop10BrandsByMallType(mallType);
        String key = "top10" + mallType + "brandList";
        return ResponseEntity.ok(Collections.singletonMap(key, top10BrandList));
    }
}
