package Designovel.Capstone.api.styleFilter.queryDSL;

import Designovel.Capstone.api.styleFilter.dto.StyleFilterDTO;
import Designovel.Capstone.domain.style.style.StyleId;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface StyleFilterQueryDSL {
    //최신 가격을 찾기 위한 마지막 크롤링 날짜 서브 쿼리 생성 메서드
    JPQLQuery<LocalDate> createLatestCrawledDateSubQuery(StyleFilterDTO filterDTO);

    //상품 RankScore 합계(노출 지수) 및 상품 아이디 DB 조회 메서드
    List<Tuple> getExposureIndexInfo(BooleanBuilder builder, Pageable pageable, String sortBy, String sortOrder);

    //노출 지수 조회 이후, 상품 가격, 브랜드 등 기타 데이터 DB 조회 메서드
    List<Tuple> getPriceInfo(BooleanBuilder builder, List<StyleId> styleIdList, StyleFilterDTO filterDTO);

    //Pagination 전체 상품 수 DB 조회 메서드
    Long getFilteredStyleCount(BooleanBuilder builder);

    //sortBy(가격, 노출 지수 등)와 sortOrder(ASC, DESC)에 따른 정렬 조건 생성 메서드
    OrderSpecifier<?> getStyleFilterOrderSpecifier(String sortBy, String sortOrder);

    //동적 필터에 따른 동적 쿼리 생성 메서드
    BooleanBuilder buildStyleFilter(StyleFilterDTO filterDTO);

}
