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
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import static Designovel.Capstone.entity.enumType.MallType.*;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/style/detail")
public class ProductDetailController {
    private final ProductService productService;
    private final MusinsaVariableService musinsaVariableService;
    private final HandsomeVariableService handsomeVariableService;
    private final HandsomeReviewService handsomeReviewService;
    private final WConceptVariableService wConceptVariableService;
    private final WConceptReviewService wConceptReviewService;

    private final MusinsaReviewService musinsaReviewService;

    @GetMapping("/detail/MUSINSA/{productId}")
    public ResponseEntity<Map<String, Object>> getMusinsaProductDetail(@PathVariable("productId") String productId) {
        Map<String, Object> response = new HashMap<>();
        ProductBasicDetailDTO productBasicDetail = productService.getProductBasicDetailDTO(productId, MUSINSA.getType());
        MusinsaVariable musinsaVariable = musinsaVariableService.getMusinsaVariable(productId);
        response.put("basicDetail", productBasicDetail);
        response.put("variable", musinsaVariable);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/detail/MUSINSA/review/{productId}")
    public ResponseEntity<Page<MusinsaReview>> getMusinsaProductDetailReview(@PathVariable("productId") String productId,
                                                                             @RequestParam int page) {
        Page<MusinsaReview> musinsaReviewPage = musinsaReviewService.findByProductId(productId, page);
        return ResponseEntity.ok(musinsaReviewPage);
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

    @GetMapping("/detail/WCONCEPT/review/{productId}")
    public ResponseEntity<Page<WConceptReview>> getWConceptProductDetailReview(@PathVariable("productId") String productId,
                                                                               @RequestParam int page) {
        Page<WConceptReview> wConceptReviewPage = wConceptReviewService.findByProductId(productId, page);
        return ResponseEntity.ok(wConceptReviewPage);
    }
}
