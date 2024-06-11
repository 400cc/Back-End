package Designovel.Capstone.service.review;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReviewProductService {


    public Map<LocalDate, Integer> createDateRangeMap(LocalDate startDate, LocalDate endDate) {
        Map<LocalDate, Integer> dateRangeMap = new LinkedHashMap<>();

        long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate);
        for (long i = 0; i <= numOfDaysBetween; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            dateRangeMap.put(currentDate, 0);
        }
        return dateRangeMap;
    }
}
