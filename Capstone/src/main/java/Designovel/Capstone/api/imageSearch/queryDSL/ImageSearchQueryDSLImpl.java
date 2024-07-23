package Designovel.Capstone.api.imageSearch.queryDSL;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.domain.category.categoryStyle.QCategoryStyle.categoryStyle;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ImageSearchQueryDSLImpl implements ImageSearchQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Optional<List<String>> findStyleByCategory(ImageSearchDTO imageSearchDTO) {
        return jpaQueryFactory
                .select(styleRanking.categoryStyle.id.styleId)
                .from(styleRanking)
                .where(styleRanking.categoryStyle.id.styleId.in(
                        JPAExpressions.select(categoryStyle.id.styleId)
                                .from(categoryStyle)
                                .join(categoryStyle.category, category)
                                .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                                .where(categoryClosure.id.ancestorId.in(imageSearchDTO.getCategoryList()))
                ))
                .fetch();
    }

}
