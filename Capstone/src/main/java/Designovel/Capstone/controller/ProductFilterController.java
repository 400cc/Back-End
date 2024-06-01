package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingDTO;
import Designovel.Capstone.entity.HandsomeReview;
import Designovel.Capstone.entity.HandsomeVariable;
import Designovel.Capstone.entity.MusinsaVariable;
import Designovel.Capstone.entity.WConceptVariable;
import Designovel.Capstone.service.category.CategoryService;
import Designovel.Capstone.service.product.ProductRankingService;
import Designovel.Capstone.service.product.ProductService;
import Designovel.Capstone.service.review.HandsomeReviewService;
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
@RequestMapping("/style")
public class ProductFilterController {

    private final ProductRankingService productRankingService;
    private final CategoryService categoryService;

    private final ProductService productService;
    private final MusinsaVariableService musinsaVariableService;
    private final HandsomeVariableService handsomeVariableService;
    private final HandsomeReviewService handsomeReviewService;
    private final WConceptVariableService wConceptVariableService;

    @GetMapping
    public ResponseEntity<Page<ProductRankingDTO>> getProductRankings(@ModelAttribute ProductFilterDTO filter, int page) {
        Page<ProductRankingDTO> productRankings = productRankingService.getProductRankingByFilter(filter, page);
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
    public ResponseEntity<Map<String, Object>> getMusinsaProductDetail(@PathVariable("productId") String productId) {
        Map<String, Object> response = new HashMap<>();
        ProductBasicDetailDTO productBasicDetail = productService.getProductBasicDetailDTO(productId, MUSINSA.getType());
        MusinsaVariable musinsaVariable = musinsaVariableService.getMusinsaVariable(productId);
        response.put("basicDetail", productBasicDetail);
        response.put("variable", musinsaVariable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/HANDSOME/{productId}")
    public ResponseEntity<Object> getHandsomeProductDetail(@PathVariable("productId") String productId) {
        Map<String, Object> response = new HashMap<>();
        ProductBasicDetailDTO productBasicDetail = productService.getProductBasicDetailDTO(productId, HANDSOME.getType());
        HandsomeVariable handsomeVariable = handsomeVariableService.getHandsomeVariableByProductId(productId);
        response.put("basicDetail", productBasicDetail);
        response.put("variable", handsomeVariable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/HANDSOME/review/{productId}")
    public ResponseEntity<Page<HandsomeReview>> getHandsomeProductDetailReview(@PathVariable("productId") String productId,
                                                                               @RequestParam int page) {
        Page<HandsomeReview> handsomeReviewPage = handsomeReviewService.findByProductId(productId, page);
        return ResponseEntity.ok(handsomeReviewPage);
    }

    @GetMapping("/detail/WCONCEPT/{productId}")
    public ResponseEntity<Object> getWConceptProductDetail(@PathVariable("productId") String productId) {
        Map<String, Object> response = new HashMap<>();
        ProductBasicDetailDTO productBasicDetail = productService.getProductBasicDetailDTO(productId, WCONCEPT.getType());
        WConceptVariable wConceptVariable = wConceptVariableService.getWConceptVariableByProductId(productId);
        response.put("basicDetail", productBasicDetail);
        response.put("variable", wConceptVariable);
        return ResponseEntity.ok(response);
    }

}
