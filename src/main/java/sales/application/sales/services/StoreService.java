package sales.application.sales.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.StoreDto;
import sales.application.sales.entities.Store;

import java.util.List;
import java.util.Map;


@Service
public class StoreService extends CommonRepository {

    public Page<Map<String,Object>> getAllStores(SearchFilters searchFilters){
        Pageable pageable = getPageable(searchFilters);
        return storeRepository.findAllStore(pageable);
    }

    public Store findStoreBySlug(String slug){
        return storeRepository.findStoreBySlug(slug);
    }
}
