package Designovel.Capstone.controller;

import Designovel.Capstone.domain.MusinsaVariableDTO;
import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.domain.ProductRankingDTO;
import Designovel.Capstone.entity.MusinsaVariable;
import Designovel.Capstone.entity.enumType.MallType;
import Designovel.Capstone.service.category.CategoryService;
import Designovel.Capstone.service.image.ImageService;
import Designovel.Capstone.service.product.ProductRankingService;
import Designovel.Capstone.service.product.ProductService;
import Designovel.Capstone.service.product.SKUAttributeService;
import Designovel.Capstone.service.variable.MusinsaVariableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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

//    @GetMapping("/detail/WCONCEPT/{productId}")
//    public ResponseEntity<Object> getWConceptProductDetail(@PathVariable("productId") String productId) {
//
//    }
//    @GetMapping("/detail/HANDSOME/{productId}")
//    public ResponseEntity<Object> getHandsomeProductDetail(@PathVariable("productId") String productId) {
//
//    }
}
