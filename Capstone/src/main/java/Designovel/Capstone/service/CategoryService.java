package Designovel.Capstone.service;

import Designovel.Capstone.domain.CategoryNode;
import Designovel.Capstone.entity.Category;
import Designovel.Capstone.entity.CategoryClosure;
import Designovel.Capstone.repository.CategoryClosureRepository;
import Designovel.Capstone.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;

    public List<CategoryNode> getCategoryTree(String mallType) {
        List<Category> categories = categoryRepository.findByMallType(mallType);
        List<CategoryClosure> closures = categoryClosureRepository.findByMallType(mallType);
        return buildCategoryTree(categories, closures);
    }

    private List<CategoryNode> buildCategoryTree(List<Category> categories, List<CategoryClosure> closures) {
        Map<Integer, CategoryNode> nodeMap = new HashMap<>();

        // 모든 카테고리를 CategoryNode로 변환하여 맵에 저장
        for (Category category : categories) {
            nodeMap.put(category.getCategoryId(), new CategoryNode(category.getCategoryId(), category.getName(), new ArrayList<>()));
        }

        // closures를 사용하여 계층 구조 빌드
        for (CategoryClosure closure : closures) {
            CategoryNode parent = nodeMap.get(closure.getAncestorId().getCategoryId());
            CategoryNode child = nodeMap.get(closure.getDescendantId().getCategoryId());

            if (!parent.equals(child)) {
                parent.getChildren().add(child);
            }
        }

        // 루트 노드 찾기
        List<CategoryNode> roots = new ArrayList<>();
        for (CategoryNode node : nodeMap.values()) {
            boolean isRoot = true;
            for (CategoryClosure closure : closures) {
                if (closure.getDescendantId().getCategoryId().equals(node.getCategoryId())
                        && closure.getDepth() == 1) {
                    isRoot = false;
                    break;
                }
            }
            if (isRoot) {
                roots.add(node);
            }
        }

        return roots;
    }
}
