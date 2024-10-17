package Designovel.Capstone.api.styleFilter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryNode {
    private Integer categoryId;
    private String name;
    //트리 구조를 위해 자기 자신을 리스트로 가짐
    private List<CategoryNode> children;
}