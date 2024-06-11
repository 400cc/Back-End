package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ReviewFilterDTO;
import Designovel.Capstone.service.review.HandsomeReviewService;
import Designovel.Capstone.service.review.MusinsaReviewService;
import Designovel.Capstone.service.review.ReviewProductService;
import Designovel.Capstone.service.review.WConceptReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "상품 상세 리뷰", description = "상품 상세 리뷰 정보 API")
@RequestMapping("/style/detail/review")
public class ProductDetailReviewController {
    private final WConceptReviewService wConceptReviewService;
    private final MusinsaReviewService musinsaReviewService;
    private final HandsomeReviewService handsomeReviewService;
    private final ReviewProductService reviewProductService;

    @Operation(summary = "한섬 상품 리뷰 조회", description = "key:count - rate1 ~ rate5 개수 조회, key:review - 리뷰 페이지로 조회")
    @GetMapping("/FHyETFQN")
    public ResponseEntity<Map<String, Object>> getHandsomeReviewCount(@ModelAttribute ReviewFilterDTO reviewFilterDTO) {
        Map<String, Object> response = handsomeReviewService.getHandsomeReviewPageByFilter(reviewFilterDTO);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "리뷰 트랜드 분석", description = "기간을 받아 해당 기간에 리뷰 개수를 반환")
    @GetMapping("/FHyETFQN/trend")
    public Map<LocalDate, Integer> getHandsomeReviewTrend(@RequestParam String productId) {
        return handsomeReviewService.getReviewTrend(productId);

    }


//    @Operation(summary = "무신사 상품 리뷰 조회", description = "특정 무신사 상품의 리뷰를 조회(페이지)")
//    @GetMapping("/JN1qnDZA/general/{productId}/{startDate}")
//    public ResponseEntity<Page<MusinsaReview>> getMusinsaProductDetailReview(@PathVariable("productId") String productId,
//                                                                             @PathVariable("startDate") Date startDate) {
//        Page<MusinsaReview> musinsaReviewPage = musinsaReviewService.findByProductId(productId, page);
//        return ResponseEntity.ok(musinsaReviewPage);
//    }


//    @Operation(summary = "W컨셉 상품 리뷰 조회", description = "특정 W컨셉 상품의 리뷰를 조회(페이지)")
//    @GetMapping("/l8WAu4fP/review/{productId}")
//    public ResponseEntity<Page<WConceptReview>> getWConceptProductDetailReview(@PathVariable("productId") String productId,
//                                                                               @RequestParam int page) {
//        Page<WConceptReview> wConceptReviewPage = wConceptReviewService.findByProductId(productId, page);
//        return ResponseEntity.ok(wConceptReviewPage);
//    }


}
