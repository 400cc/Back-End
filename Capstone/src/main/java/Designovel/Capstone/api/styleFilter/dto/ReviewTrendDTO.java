package Designovel.Capstone.api.styleFilter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewTrendDTO {

    private LocalDate date;
    private Integer reviewCount;
}
