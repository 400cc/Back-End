package Designovel.Capstone.api.styleFilter.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CategoryNode {
    private Integer categoryId;
    private String name;
    private List<CategoryNode> children;

}