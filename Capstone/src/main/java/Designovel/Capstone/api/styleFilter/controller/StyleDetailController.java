package Designovel.Capstone.api.styleFilter.controller;

import Designovel.Capstone.api.styleFilter.dto.StyleBasicDetailDTO;
import Designovel.Capstone.api.styleFilter.service.StyleDetailService;
import Designovel.Capstone.domain.variable.handsomeVariable.HandsomeVariable;
import Designovel.Capstone.domain.variable.musinsaVariable.MusinsaVariable;
import Designovel.Capstone.domain.variable.wconceptVariable.WConceptVariable;
import Designovel.Capstone.domain.variable.handsomeVariable.HandsomeVariableService;
import Designovel.Capstone.domain.variable.musinsaVariable.MusinsaVariableService;
import Designovel.Capstone.domain.variable.wconceptVariable.WConceptVariableService;
import Designovel.Capstone.global.exception.CustomException;
import Designovel.Capstone.global.exception.ErrorCode;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static Designovel.Capstone.domain.mallType.enumType.MallTypeId.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "상품 상세", description = "상품 상세 정보 API")
@RequestMapping("/style/detail")
public class StyleDetailController {
    private final StyleDetailService styleDetailService;
    private final MusinsaVariableService musinsaVariableService;
    private final HandsomeVariableService handsomeVariableService;

    private final WConceptVariableService wConceptVariableService;


    @Operation(summary = "무신사 상품 상세 정보 조회", description = "특정 무신사 상품의 상세 정보(리뷰 제외) 조회")
    @GetMapping("/JN1qnDZA/{styleId}")
    public ResponseEntity<Map<String, Object>> getMusinsaStyleDetail(@PathVariable("styleId") String styleId) {
        if (styleId == null || styleId.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.STYLE_ID_IS_NULL);
        }

        StyleBasicDetailDTO styleBasicDetail = styleDetailService.getStyleBasicDetailDTO(styleId, MUSINSA.getType());
        MusinsaVariable musinsaVariable = musinsaVariableService.getMusinsaVariable(styleId);
        Map<String, Object> response = Map.of(
                "basicDetail", styleBasicDetail,
                "variable", musinsaVariable
        );
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "한섬 상품 상세 정보 조회", description = "특정 한섬 상품의 상세 정보(리뷰 제외) 조회")
    @ApiResponse(responseCode = "400", description = "스타일 ID가 존재하지 않을 때",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"errorMessage\":\"스타일 ID를 입력해야 합니다.\"}")))
    @GetMapping("/FHyETFQN/{styleId}")
    public ResponseEntity<Object> getHandsomeProductDetail(@PathVariable("styleId") String styleId) {
        if (styleId == null || styleId.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.STYLE_ID_IS_NULL);
        }

        StyleBasicDetailDTO styleBasicDetail = styleDetailService.getStyleBasicDetailDTO(styleId, HANDSOME.getType());
        HandsomeVariable handsomeVariable = handsomeVariableService.getHandsomeVariableByStyleId(styleId);
        Map<String, Object> response = Map.of(
                "basicDetail", styleBasicDetail,
                "variable", handsomeVariable
        );
        return ResponseEntity.ok(response);
    }


    @Operation(summary = "W컨셉 상품 상세 정보 조회", description = "특정 W컨셉 상품의 상세 정보(리뷰 제외) 조회")
    @GetMapping("/l8WAu4fP/{styleId}")
    public ResponseEntity<Object> getWConceptProductDetail(@PathVariable("styleId") String styleId) {
        if (styleId == null || styleId.isEmpty()) {
            throw new CustomException(HttpStatus.BAD_REQUEST, ErrorCode.STYLE_ID_IS_NULL);
        }
        StyleBasicDetailDTO styleBasicDetail = styleDetailService.getStyleBasicDetailDTO(styleId, WCONCEPT.getType());
        WConceptVariable wConceptVariable = wConceptVariableService.getWConceptVariableByStyleId(styleId);
        Map<String, Object> response = Map.of(
                "basicDetail", styleBasicDetail,
                "variable", wConceptVariable
        );
        return ResponseEntity.ok(response);
    }


}
