package Designovel.Capstone.api.imageSearch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImageSearchDTO {

    private MultipartFile image;
    private List<Integer> categoryList;
    private String categoryNameList;
    private String mallTypeId;
    private int offset;

    public ImageSearchDTO(MultipartFile image, String categoryListStr, String mallTypeId, int offset, String categoryNameListStr) {
        this.image = image;
        this.mallTypeId = mallTypeId;
        this.offset = offset;
        this.categoryList = convertCategoryList(categoryListStr);
        this.categoryNameList = convertCategoryNameList(categoryNameListStr);
    }

    public List<Integer> convertCategoryList(String categoryListStr) {
        if (categoryListStr == null || categoryListStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(categoryListStr.replace("[", "").replace("]", "").split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    public String convertCategoryNameList(String categoryNameListStr) {
        if (categoryNameListStr == null || categoryNameListStr.trim().isEmpty()) {
            return "";
        }
        return Arrays.stream(categoryNameListStr.replace("[", "").replace("]", "").split(","))
                .map(String::trim)
                .collect(Collectors.joining(", "));
    }
}
