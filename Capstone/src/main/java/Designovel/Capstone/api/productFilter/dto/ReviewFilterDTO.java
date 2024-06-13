package Designovel.Capstone.api.productFilter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewFilterDTO {
    private String productId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    private List<Integer> rate; //복수
    private Integer page;
}
