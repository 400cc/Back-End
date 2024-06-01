package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingDTO;
import Designovel.Capstone.entity.*;
import Designovel.Capstone.service.category.CategoryService;
import Designovel.Capstone.service.product.ProductRankingService;
import Designovel.Capstone.service.product.ProductService;
import Designovel.Capstone.service.review.HandsomeReviewService;
import Designovel.Capstone.service.review.WConceptReviewService;
import Designovel.Capstone.service.variable.HandsomeVariableService;
import Designovel.Capstone.service.variable.MusinsaVariableService;
import Designovel.Capstone.service.variable.WConceptVariableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static Designovel.Capstone.entity.enumType.MallType.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/style/filter")
public class ProductFilterController {

    private final ProductRankingService productRankingService;
    private final CategoryService categoryService;


    @GetMapping
    public ResponseEntity<Page<ProductRankingDTO>> getProductRankings(@ModelAttribute ProductFilterDTO filter, int page) {
        Page<ProductRankingDTO> productRankings = productRankingService.getProductRankingByFilter(filter, page);
        return ResponseEntity.ok(productRankings);
    }

    @GetMapping("/category/{mallType}")
    public ResponseEntity<Object> getCategories(@PathVariable("mallType") String mallType) {
        return ResponseEntity.ok(categoryService.getCategoryTree(mallType));
    }

    @GetMapping("/brand/{mallType}")
    public ResponseEntity<Object> getBrands(@PathVariable("mallType") String mallType) {
        Map<String, List<String>> distinctBrandMap = new HashMap<>();
        distinctBrandMap.put("brand", productRankingService.getBrands(mallType));
        return ResponseEntity.ok(distinctBrandMap);
    }


}
