package Designovel.Capstone.controller;

import Designovel.Capstone.domain.HandsomeReviewDTO;
import Designovel.Capstone.domain.ReviewCountDTO;
import Designovel.Capstone.entity.WConceptReview;
import Designovel.Capstone.service.review.HandsomeReviewService;
import Designovel.Capstone.service.review.MusinsaReviewService;
import Designovel.Capstone.service.review.WConceptReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "상품 상세 리뷰", description = "상품 상세 리뷰 정보 API")
@RequestMapping("/style/detail/review")
public class ProductDetailReviewController {
    private final WConceptReviewService wConceptReviewService;
    private final MusinsaReviewService musinsaReviewService;
    private final HandsomeReviewService handsomeReviewService;

//    @Operation(summary = "무신사 상품 리뷰 조회", description = "특정 무신사 상품의 리뷰를 조회(페이지)")
//    @GetMapping("/JN1qnDZA/general/{productId}/{startDate}")
//    public ResponseEntity<Page<MusinsaReview>> getMusinsaProductDetailReview(@PathVariable("productId") String productId,
//                                                                             @PathVariable("startDate") Date startDate) {
//        Page<MusinsaReview> musinsaReviewPage = musinsaReviewService.findByProductId(productId, page);
//        return ResponseEntity.ok(musinsaReviewPage);
//    }

    @Operation(summary = "한섬 상품 리뷰 개수 조회", description = "특정 한섬 상품의 별점 별 리뷰 개수 조회 - 상품ID와 날짜로 조회")
    @GetMapping("/FHyETFQN/general/{productId}")
    public ResponseEntity<ReviewCountDTO> getHandsomeReviewCount(@PathVariable("productId") String productId,
                                                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate) {
        ReviewCountDTO reviewCountDTO = handsomeReviewService.findReviewCountByProductId(productId, startDate);
        return ResponseEntity.ok(reviewCountDTO);
    }

    @Operation(summary = "한섬 상품 리뷰 조회", description = "특정 한섬 상품의 리뷰를 조회(페이지)")
    @GetMapping("/FHyETFQN/precise/{productId}")
    public ResponseEntity<Page<HandsomeReviewDTO>> getHandsomeReviewByRating(@PathVariable("productId") String productId,
                                                                             @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDate,
                                                                             @RequestParam("page") int page,
                                                                             @RequestParam("rate") int rate) {
        Page<HandsomeReviewDTO> handsomeReviewDTOPage = handsomeReviewService.findReviewByProductIdAndRate(productId, startDate, page, rate);
        return ResponseEntity.ok(handsomeReviewDTOPage);
    }



    @Operation(summary = "W컨셉 상품 리뷰 조회", description = "특정 W컨셉 상품의 리뷰를 조회(페이지)")
    @GetMapping("/l8WAu4fP/review/{productId}")
    public ResponseEntity<Page<WConceptReview>> getWConceptProductDetailReview(@PathVariable("productId") String productId,
                                                                               @RequestParam int page) {
        Page<WConceptReview> wConceptReviewPage = wConceptReviewService.findByProductId(productId, page);
        return ResponseEntity.ok(wConceptReviewPage);
    }
}
