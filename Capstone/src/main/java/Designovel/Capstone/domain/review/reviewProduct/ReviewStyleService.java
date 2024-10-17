package Designovel.Capstone.domain.review.reviewProduct;

import Designovel.Capstone.api.styleFilter.dto.ReviewCountDTO;
import Designovel.Capstone.api.styleFilter.dto.ReviewTrendDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewStyleService {


    /**
     * 작성일 별 리뷰 수 처리 메서드
     * 1. 작성일을 Key로 하고 리뷰 수를 Value로 하는 Map 생성
     * 2. Map에 값 할당
     * 3. ReviewTrendDTO로 변환
     * @param queryResult
     * @return 변환된 ReviwTrendDTO 반환
     */
    public List<ReviewTrendDTO> processReviewTrendQueryResult(List<Object[]> queryResult) {
        LocalDate startDate = queryResult.isEmpty() ? null : (LocalDate) queryResult.get(0)[0];
        LocalDate endDate = queryResult.isEmpty() ? null : (LocalDate) queryResult.get(queryResult.size() - 1)[0];
        //날짜 별 Map 생성
        Map<LocalDate, Integer> dateRangeMap = createDateRangeMap(startDate, endDate);
        for (Object[] object : queryResult) {
            LocalDate writtenDate = (LocalDate) object[0];
            Long longReviewCount = (Long) object[1];
            Integer reviewCount = (longReviewCount != null) ? longReviewCount.intValue() : 0;
            dateRangeMap.put(writtenDate, reviewCount);
        }

        return convertToReviewTrendDTO(dateRangeMap);
    }


    /**
     * 날짜 별 Map 생성 메서드
     * - 각 쇼핑몰 별 DB 조회 한 날짜를 Key로 하여 Map 생성
     * - 값은 0으로 초기 설정
     * @param startDate
     * @param endDate
     * @return 생성된 Map 반환
     */
    public Map<LocalDate, Integer> createDateRangeMap(LocalDate startDate, LocalDate endDate) {
        //값이 없으면 빈 Map 반환
        if (startDate == null || endDate == null) {
            return new LinkedHashMap<>();
        }
        Map<LocalDate, Integer> dateRangeMap = new LinkedHashMap<>();

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        for (long i = 0; i <= numOfDaysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            dateRangeMap.put(currentDate, 0);
        }
        return dateRangeMap;
    }


    /**
     * Map을 ReviewTrendDTO로 변환하는 메서드
     * @param dateRangeMap
     * @return 변환된 ReviewTrendDTO 반환
     */
    public List<ReviewTrendDTO> convertToReviewTrendDTO(Map<LocalDate, Integer> dateRangeMap) {
        return dateRangeMap.entrySet().stream()
                .map(entry -> new ReviewTrendDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    /**
     * ReviewCountDTO 생성 메서드
     * - Map을 ReviewCountDTO로 변환
     * @param ratingCountMap
     * @param total
     * @return 변환된 ReviewCountDTO 반환
     */
    public ReviewCountDTO createReviewCountDTO(Map<Integer, Integer> ratingCountMap, int total) {
        ReviewCountDTO reviewCountDTO = new ReviewCountDTO();
        reviewCountDTO.setRate1(ratingCountMap.getOrDefault(1, 0));
        reviewCountDTO.setRate2(ratingCountMap.getOrDefault(2, 0));
        reviewCountDTO.setRate3(ratingCountMap.getOrDefault(3, 0));
        reviewCountDTO.setRate4(ratingCountMap.getOrDefault(4, 0));
        reviewCountDTO.setRate5(ratingCountMap.getOrDefault(5, 0));
        reviewCountDTO.setTotal(total);
        return reviewCountDTO;
    }

}
