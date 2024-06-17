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


    public Map<LocalDate, Integer> createDateRangeMap(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Integer> dateRangeMap = new LinkedHashMap<>();

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        for (long i = 0; i <= numOfDaysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            dateRangeMap.put(currentDate, 0);
        }
        return dateRangeMap;
    }

    public List<ReviewTrendDTO> processReviewTrendQueryResult(List<Object[]> queryResult) {
        LocalDate startDate = queryResult.isEmpty() ? null : (LocalDate) queryResult.get(0)[0];
        LocalDate endDate = queryResult.isEmpty() ? null : (LocalDate) queryResult.get(queryResult.size() - 1)[0];
        Map<LocalDate, Integer> dateRangeMap = createDateRangeMap(startDate, endDate);
        for (Object[] object : queryResult) {
            LocalDate crawledDate = (LocalDate) object[0];
            Long longReviewCount = (Long) object[1];
            Integer reviewCount = (longReviewCount != null) ? longReviewCount.intValue() : 0;
            dateRangeMap.put(crawledDate, reviewCount);
        }
        List<ReviewTrendDTO> ReviewTrendDTOList = convertToReviewTrendDTO(dateRangeMap);

        return ReviewTrendDTOList;
    }

    public List<ReviewTrendDTO> convertToReviewTrendDTO(Map<LocalDate, Integer> dateRangeMap) {
        return dateRangeMap.entrySet().stream()
                .map(entry -> new ReviewTrendDTO(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }


    public static ReviewCountDTO createReviewCountDTO(Map<Integer, Integer> ratingCountMap, int total) {
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
