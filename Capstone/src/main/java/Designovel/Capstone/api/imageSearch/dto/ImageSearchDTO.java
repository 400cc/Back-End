package Designovel.Capstone.api.imageSearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@AllArgsConstructor
public class ImageSearchDTO {

    private MultipartFile image;
    private List<Integer> categoryList;
    private String mallTypeId;
    private int offset;

}
