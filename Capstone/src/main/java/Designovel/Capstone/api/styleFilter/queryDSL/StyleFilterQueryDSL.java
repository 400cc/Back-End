package Designovel.Capstone.api.styleFilter.queryDSL;

import Designovel.Capstone.api.styleFilter.dto.StyleFilterDTO;
import Designovel.Capstone.domain.style.style.StyleId;
import Designovel.Capstone.domain.style.styleRanking.QStyleRanking;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface StyleFilterQueryDSL {
    JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(QStyleRanking subStyleRanking);

    QueryResults<Tuple> getExposureIndexInfo(BooleanBuilder builder, Pageable pageable, String sortBy, String sortOrder);

    List<Tuple> getPriceInfo(BooleanBuilder builder, List<StyleId> styleIdList);

    OrderSpecifier<?> getStyleFilterOrderSpecifier(String sortBy, String sortOrder);

    BooleanBuilder buildStyleFilter(StyleFilterDTO filterDTO);

}
