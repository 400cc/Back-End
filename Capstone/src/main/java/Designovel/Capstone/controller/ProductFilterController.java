package Designovel.Capstone.controller;

import Designovel.Capstone.domain.ProductFilterDTO;
import Designovel.Capstone.entity.ProductRanking;
import Designovel.Capstone.service.ProductRankingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductFilterController {

    private final ProductRankingService productRankingService;

    @GetMapping("/filter")
    public ResponseEntity<List<ProductRanking>> getProductRankings(@ModelAttribute ProductFilterDTO filter) {
        List<ProductRanking> productRankings = productRankingService.getProductRankings(filter);
        return ResponseEntity.ok(productRankings);
    }

}
