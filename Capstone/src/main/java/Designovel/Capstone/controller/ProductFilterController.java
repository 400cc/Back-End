package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.service.CategoryService;
import Designovel.Capstone.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductFilterController {

    private final ProductRankingService productRankingService;
    private final CategoryService categoryService;

    @GetMapping("/filter")
    public ResponseEntity<Map<String, ProductRankingAvgDTO>> getProductRankings(@ModelAttribute ProductFilterDTO filter) {
        Map<String, ProductRankingAvgDTO> productRankings = productRankingService.getProductRankingAverages(filter);
        return ResponseEntity.ok(productRankings);
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getCategories(@RequestParam String mallType) {
        return ResponseEntity.ok(categoryService.getCategoryTree(mallType));
    }

}
