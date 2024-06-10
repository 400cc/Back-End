package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductBasicDetailDTO;
import Designovel.Capstone.entity.*;
import Designovel.Capstone.service.product.ProductService;
import Designovel.Capstone.service.review.HandsomeReviewService;
import Designovel.Capstone.service.review.MusinsaReviewService;
import Designovel.Capstone.service.review.WConceptReviewService;
import Designovel.Capstone.service.variable.HandsomeVariableService;
import Designovel.Capstone.service.variable.MusinsaVariableService;
import Designovel.Capstone.service.variable.WConceptVariableService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

import static Designovel.Capstone.entity.enumType.MallTypeId.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "상품 상세", description = "상품 상세 정보 API")
@RequestMapping("/style/detail")
public class ProductDetailController {
    private final ProductService productService;
    private final MusinsaVariableService musinsaVariableService;
    private final HandsomeVariableService handsomeVariableService;

    private final WConceptVariableService wConceptVariableService;


    @Operation(summary = "무신사 상품 상세 정보 조회", description = "특정 무신사 상품의 상세 정보(리뷰 제외) 조회")
    @GetMapping("/JN1qnDZA/{productId}")
    public ResponseEntity<Map<String, Object>> getMusinsaProductDetail(@PathVariable("productId") String productId) {
        Map<String, Object> response = new HashMap<>();
        ProductBasicDetailDTO productBasicDetail = productService.getProductBasicDetailDTO(productId, MUSINSA.getType());
        MusinsaVariable musinsaVariable = musinsaVariableService.getMusinsaVariable(productId);
        response.put("basicDetail", productBasicDetail);
        response.put("variable", musinsaVariable);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "한섬 상품 상세 정보 조회", description = "특정 한섬 상품의 상세 정보(리뷰 제외) 조회")
    @GetMapping("/FHyETFQN/{productId}")
    public ResponseEntity<Object> getHandsomeProductDetail(@PathVariable("productId") String productId) {
        Map<String, Object> response = new HashMap<>();
        ProductBasicDetailDTO productBasicDetail = productService.getProductBasicDetailDTO(productId, HANDSOME.getType());
        HandsomeVariable handsomeVariable = handsomeVariableService.getHandsomeVariableByProductId(productId);
        response.put("basicDetail", productBasicDetail);
        response.put("variable", handsomeVariable);
        return ResponseEntity.ok(response);
    }



    @Operation(summary = "W컨셉 상품 상세 정보 조회", description = "특정 W컨셉 상품의 상세 정보(리뷰 제외) 조회")
    @GetMapping("/l8WAu4fP/{productId}")
    public ResponseEntity<Object> getWConceptProductDetail(@PathVariable("productId") String productId) {
        Map<String, Object> response = new HashMap<>();
        ProductBasicDetailDTO productBasicDetail = productService.getProductBasicDetailDTO(productId, WCONCEPT.getType());
        WConceptVariable wConceptVariable = wConceptVariableService.getWConceptVariableByProductId(productId);
        response.put("basicDetail", productBasicDetail);
        response.put("variable", wConceptVariable);
        return ResponseEntity.ok(response);
    }


}
