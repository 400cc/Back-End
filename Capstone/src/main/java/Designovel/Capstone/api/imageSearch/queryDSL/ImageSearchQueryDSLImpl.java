package Designovel.Capstone.api.imageSearch.queryDSL;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static Designovel.Capstone.domain.category.category.QCategory.category;
import static Designovel.Capstone.domain.category.categoryClosure.QCategoryClosure.categoryClosure;
import static Designovel.Capstone.domain.style.styleRanking.QStyleRanking.styleRanking;

@RequiredArgsConstructor
@Slf4j
@Repository
public class ImageSearchQueryDSLImpl implements ImageSearchQueryDSL {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<String> findStyleByCategory(ImageSearchDTO imageSearchDTO) {
        return jpaQueryFactory
                .select(styleRanking.styleId).distinct()
                .from(styleRanking)
                .where(styleRanking.styleId.in(
                        JPAExpressions.select(styleRanking.styleId)
                                .from(styleRanking)
                                .join(category)
                                .on(styleRanking.categoryId.eq(category.categoryId))
                                .join(categoryClosure).on(categoryClosure.id.descendantId.eq(category.categoryId))
                                .where(categoryClosure.id.ancestorId.in(imageSearchDTO.getCategoryList()))
                ))
                .fetch();
    }

}
