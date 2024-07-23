package Designovel.Capstone.api.imageSearch.queryDSL;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;
import com.querydsl.core.BooleanBuilder;

import java.util.List;
import java.util.Optional;

public interface ImageSearchQueryDSL {
    Optional<List<String>> findStyleByCategory(ImageSearchDTO imageSearchDTO);
}
