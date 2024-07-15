package Designovel.Capstone.domain.category.category;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public String findNameByCategoryIdList(List<Integer> categoryIdList) {
        List<String> categoryNameList = categoryRepository.findNameByCategoryIdList(categoryIdList);
        return String.join(", ", categoryNameList);
    }
}
