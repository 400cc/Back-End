package Designovel.Capstone.api.styleFilter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewCountDTO {
    private int rate1 = 0;
    private int rate2 = 0;
    private int rate3 = 0;
    private int rate4 = 0;
    private int rate5 = 0;
    private int total = 0;
}
