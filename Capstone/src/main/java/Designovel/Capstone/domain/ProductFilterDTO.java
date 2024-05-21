package Designovel.Capstone.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ProductFilterDTO {
    private List<String> category;
    private List<String> brand;
    private Date startDate;
    private Date endDate;
    private String site;

}
