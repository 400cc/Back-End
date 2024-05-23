package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingAvgDTO;
import Designovel.Capstone.entity.Category;
import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.service.CategoryService;
import Designovel.Capstone.service.ProductRankingService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductFilterController {

    private final ProductRankingService productRankingService;
    private final CategoryService categoryService;

    @GetMapping("/filter")
    public ResponseEntity<List<ProductRankingAvgDTO>> getProductRankings(@ModelAttribute ProductFilterDTO filter) {
        List<ProductRankingAvgDTO> productRankings = productRankingService.getProductRankingAverages(filter);
        return ResponseEntity.ok(productRankings);
    }

    @GetMapping("/category")
    public ResponseEntity<Object> getCategories(@RequestParam String mallType) {
        return ResponseEntity.ok(categoryService.getCategoryTree(mallType));
    }

}
