package Designovel.Capstone.domain;

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