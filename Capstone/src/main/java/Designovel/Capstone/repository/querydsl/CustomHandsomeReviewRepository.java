package Designovel.Capstone.repository.querydsl;

import Designovel.Capstone.domain.HandsomeReviewDTO;
import Designovel.Capstone.domain.ReviewFilterDTO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomHandsomeReviewRepository {
    List<Tuple> findHandsomeReviewCountsByFilter(ReviewFilterDTO filterDTO);

    Page<HandsomeReviewDTO> findHandsomeReviewPageByFilter(ReviewFilterDTO filterDTO);

    BooleanBuilder buildHandsomeReviewFilter(ReviewFilterDTO filterDTO);
}
