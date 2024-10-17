package Designovel.Capstone.api.styleFilter.service;


import Designovel.Capstone.api.styleFilter.dto.CategoryNode;
import Designovel.Capstone.domain.category.category.Category;
import Designovel.Capstone.domain.category.category.CategoryRepository;
import Designovel.Capstone.domain.category.categoryClosure.CategoryClosure;
import Designovel.Capstone.domain.category.categoryClosure.CategoryClosureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CategoryNodeService {
    private final CategoryRepository categoryRepository;
    private final CategoryClosureRepository categoryClosureRepository;

    /**
     * 쇼핑몰에 따른 카테고리 계층도 조회 메서드
     * 1. 쇼핑몰에 맞는 카테고리 조회
     * 2. 쇼핑몰에 맞는 카테고리 클로저 테이블 조회
     * 3. 트리 구조 생성 후 반환
     * @param mallTypeId
     * @return CategoryNode의 필드로 List<CategoryNode>(자신의 리스트)가 있어 트리 구조 형태로 반환
     */
    public List<CategoryNode> getCategoryTree(String mallTypeId) {
        List<Category> categories = categoryRepository.findByMallType_MallTypeId(mallTypeId);
        List<CategoryClosure> closures = categoryClosureRepository.findByMallType_MallTypeId(mallTypeId);
        return buildCategoryTree(categories, closures);
    }

    /**
     * 카테고리 트리 구조 생성 메서드
     * @param categories 쇼핑몰에 해당하는 카테고리 리스트
     * @param closures 카테고리 간의 계층 관계를 나타내는 클로저 테이블 리스트
     * @return 최상위 노드들을 포함한 카테고리 트리 구조
     */
    private List<CategoryNode> buildCategoryTree(List<Category> categories, List<CategoryClosure> closures) {
        Map<Integer, CategoryNode> nodeMap = new HashMap<>();

        // 모든 카테고리를 CategoryNode로 변환하여 맵에 저장
        for (Category category : categories) {
            nodeMap.put(category.getCategoryId(), new CategoryNode(category.getCategoryId(), category.getName(), new ArrayList<>()));
        }

        // closures를 사용하여 계층 구조 빌드
        addChildNodeToParentNode(closures, nodeMap);
        return findRootNodes(closures, nodeMap);
    }

    /**
     * 자식 노드를 부모 노드에 추가하는 메서드
     * - 클로저 테이블을 이용하여 각 카테고리 노드 간의 부모-자식 관계 설정
     * - depth가 1인 클로저만 처리하여 직접적인 부모-자식 관계를 설정
     * @param closures 카테고리 계층 정보를 담고 있는 클로저 테이블 리스트
     * @param nodeMap 카테고리 ID를 키로 하는 노드 맵 (부모-자식 관계를 설정할 대상)
     */
    public void addChildNodeToParentNode(List<CategoryClosure> closures, Map<Integer, CategoryNode> nodeMap) {
        for (CategoryClosure closure : closures) {
            CategoryNode parent = nodeMap.get(closure.getAncestorId().getCategoryId());
            CategoryNode child = nodeMap.get(closure.getDescendantId().getCategoryId());

            if (!parent.equals(child) && closure.getDepth() == 1) {
                parent.getChildren().add(child);
            }
        }
    }

    /**
     * 루트 노드를 찾는 메서드
     * 클로저 테이블에서 depth가 1인 항목의 자손 노드 ID를 통해 최상위 노드(루트 노드) 식별
     * 최상위 노드는 다른 노드의 자손이 아니므로, 자손 노드 집합에 포함되지 않은 노드를 찾음
     * @param closures 카테고리 계층 정보를 담고 있는 클로저 테이블 리스트
     * @param nodeMap 카테고리 ID를 키로 하는 노드 맵
     * @return 최상위 루트 노드 리스트
     */
    private static List<CategoryNode> findRootNodes(List<CategoryClosure> closures, Map<Integer, CategoryNode> nodeMap) {
        Set<Integer> descendantNodeIds = new HashSet<>();
        List<CategoryNode> roots = new ArrayList<>();

        for (CategoryClosure closure : closures) {
            //depth가 1인것의 descendant들은 무조건 Root노드가 아님
            if (closure.getDepth() == 1) {
                descendantNodeIds.add(closure.getDescendantId().getCategoryId());
            }
        }
        for (CategoryNode node : nodeMap.values()) {
            // node의 ID가 자손 ID 집합에 포함되지 않으면 최상위 노드
            if (!descendantNodeIds.contains(node.getCategoryId())) {
                roots.add(node);
            }
        }
        return roots;
    }

}
