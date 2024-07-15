package Designovel.Capstone.api.imageSearch.queryDSL;

import Designovel.Capstone.api.imageSearch.dto.ImageSearchDTO;

import java.util.List;

public interface ImageSearchQueryDSL {
    List<String> findStyleByCategory(ImageSearchDTO imageSearchDTO);
}
