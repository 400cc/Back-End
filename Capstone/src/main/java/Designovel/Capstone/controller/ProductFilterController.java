package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.service.CategoryService;
import Designovel.Capstone.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductFilterController {

    private final ProductRankingService productRankingService;
    private final CategoryService categoryService;

    @GetMapping("/filter")
    public ResponseEntity<Page<ProductRankingAvgDTO>> getProductRankings(@ModelAttribute ProductFilterDTO filter, int page) {
        Page<ProductRankingAvgDTO> productRankings = productRankingService.getProductRankingAverages(filter, page);
        return ResponseEntity.ok(productRankings);
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getCategories(@RequestParam String mallType) {
        return ResponseEntity.ok(categoryService.getCategoryTree(mallType));
    }

}
