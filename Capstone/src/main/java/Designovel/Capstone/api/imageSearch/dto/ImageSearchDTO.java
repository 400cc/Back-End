package Designovel.Capstone.api.imageSearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageSearchDTO {

    private MultipartFile image;
    private List<Integer> categoryList;
    private String mallTypeId;
    private int offset;

    public ImageSearchDTO(MultipartFile image, String categoryListStr, String mallTypeId, int offset) {
        this.image = image;
        this.mallTypeId = mallTypeId;
        this.offset = offset;
        this.categoryList = convertCategoryList(categoryListStr);
    }

    public List<Integer> convertCategoryList(String categoryListStr) {
        return Arrays.stream(categoryListStr.replace("[", "").replace("]", "").split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

}
