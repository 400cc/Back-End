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

    /**
     * CategoryList(Integer) 리스트 변환 메서드
     * - 클라이언트에서 Multipart로 데이터를 보내므로 이를 ArrayList로 변환
     * @param categoryListStr
     * @return 값이 없을 경우 빈 리스트, 있을 경우 리스트로 변환하여 반환
     */
    public List<Integer> convertCategoryList(String categoryListStr) {
        if (categoryListStr == null || categoryListStr.trim().isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(categoryListStr.replace("[", "").replace("]", "").split(","))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }

    /**
     * CategoryNameList(String) Join 메서드
     * - 이미지 검색 누끼를 따기 위한 프롬프트로 CategoryName이 필요함
     * - 클라이언트에서 리스트 형태로 카테고리 이름 리스트가 오면 이를 ", "로 Join 시켜 하나의 String으로 만듦
     * @param categoryNameListStr
     * @return 값이 없으면 빈 String, 있으면 ","로 Join 시켜 반환
     */
    public String convertCategoryNameList(String categoryNameListStr) {
        if (categoryNameListStr == null || categoryNameListStr.trim().isEmpty()) {
            return "";
        }
        return Arrays.stream(categoryNameListStr.replace("[", "").replace("]", "").split(","))
                .map(String::trim)
                .collect(Collectors.joining(","));
    }
}
