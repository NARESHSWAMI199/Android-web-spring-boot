package sales.application.sales.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Store;

import java.util.Map;

import static sales.application.sales.specifications.StoreSpecifications.containsName;


@Service
public class StoreService extends CommonRepository {

    public Page<Map<String,Object>> getAllStores(SearchFilters searchFilters){
        Pageable pageable = getPageable(searchFilters);
        return storeRepository.findAllStore(pageable,searchFilters.getSearchKey());
    }


    /** if you need more filters in future */
    public Page<Store> getAllStore(SearchFilters filters) {
        Specification<Store> specification = Specification.where(
                (containsName(filters.getSearchKey()))
        );
        Pageable pageable = getPageable(filters);
        return storeRepository.findAll(specification,pageable);
    }

    public Store findStoreBySlug(String slug){
        return storeRepository.findStoreBySlug(slug);
    }
}
