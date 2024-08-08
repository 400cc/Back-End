package Designovel.Capstone.api.home.queryDSL;

import Designovel.Capstone.api.home.dto.HomeFilterDTO;

import java.util.List;
import java.util.Map;

public interface PriceRangeQueryDSL {

    String buildPriceRangeFilter(HomeFilterDTO filterDTO, Map<String, Object> params);

    List<Integer> findDiscountedPriceByFilter(HomeFilterDTO filterDTO);
}
