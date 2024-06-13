package Designovel.Capstone.api.styleFilter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class StyleFilterDTO {
    private List<Integer> category;
    private List<String> brand;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String mallTypeId;
    private String sortBy;
    private String sortOrder;

    public String getSortOrder() {
        if (sortOrder == null || sortOrder.isEmpty()) {
            return "desc"; // 기본 정렬 순서
        }
        return sortOrder;
    }

    public String getSortBy() {
        if (sortBy == null || sortBy.isEmpty()) {
            return "exposureIndex"; // 기본 정렬 컬럼
        }
        return sortBy;
    }
}
