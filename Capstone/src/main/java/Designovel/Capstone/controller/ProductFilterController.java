package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductDetailDTO;
import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingDTO;
import Designovel.Capstone.service.category.CategoryService;
import Designovel.Capstone.service.product.ProductRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/style")
public class ProductFilterController {

    private final ProductRankingService productRankingService;
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<Page<ProductRankingDTO>> getProductRankings(@ModelAttribute ProductFilterDTO filter, int page) {
        Page<ProductRankingDTO> productRankings = productRankingService.getProducRankingByFilter(filter, page);
        return ResponseEntity.ok(productRankings);
    }

    @GetMapping("/filter/category/{mallType}")
    public ResponseEntity<Object> getCategories(@PathVariable("mallType") String mallType) {
        return ResponseEntity.ok(categoryService.getCategoryTree(mallType));
    }

    @GetMapping("/filter/brand/{mallType}")
    public ResponseEntity<Object> getBrands(@PathVariable("mallType") String mallType) {
        Map<String, List<String>> distinctBrandMap = new HashMap<>();
        distinctBrandMap.put("brand", productRankingService.getBrands(mallType));
        return ResponseEntity.ok(distinctBrandMap);
    }

    @GetMapping("/detail/MUSINSA/{productId}")
    public ResponseEntity<ProductDetailDTO> getMusinsaProductDetail(@PathVariable("productId") String productId) {
        return productRankingService.getMusinsaProductDetail(productId);
    }
//    @GetMapping("/detail/WCONCEPT/{productId}")
//    public ResponseEntity<Object> getWConceptProductDetail(@PathVariable("productId") String productId) {
//
//    }
//    @GetMapping("/detail/HANDSOME/{productId}")
//    public ResponseEntity<Object> getHandsomeProductDetail(@PathVariable("productId") String productId) {
//
//    }
}
