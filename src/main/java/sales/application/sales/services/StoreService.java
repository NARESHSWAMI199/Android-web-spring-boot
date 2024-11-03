package sales.application.sales.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.ItemCategory;
import sales.application.sales.entities.Store;
import sales.application.sales.entities.StoreCategory;
import sales.application.sales.entities.StoreSubCategory;

import java.util.List;
import java.util.Map;

import static sales.application.sales.specifications.StoreSpecifications.isCategory;
import static sales.application.sales.specifications.StoreSpecifications.isSubcategory;
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
                .and(isCategory(filters.getCategoryId()))
                .and(isSubcategory(filters.getSubcategoryId()))
        );
        Pageable pageable = getPageable(filters);
        return storeRepository.findAll(specification,pageable);
    }

    public Store findStoreBySlug(String slug){
        return storeRepository.findStoreBySlug(slug);
    }

    public List<StoreCategory> getAllStoreCategories(SearchFilters searchFilters) {
        Sort sort = Sort.by(searchFilters.getOrderBy());
           sort = searchFilters.getOrder().equals("asc") ? sort.ascending():sort.descending() ;
        return storeCategoryRepository.findAll(sort);
    }

    public List<StoreSubCategory> getAllStoreSubCategories(SearchFilters searchFilters) {
        Pageable pageable = getPageable(searchFilters);
        if(searchFilters.getCategoryId() != null){
            return storeSubCategoryRepository.getSubCategories(searchFilters.getCategoryId(),pageable);
        }else {
            return storeSubCategoryRepository.findAll(pageable).getContent();
        }
    }



    public StoreSubCategory getStoreSubcategoryDetail(Integer subcategoryId) {
        return storeSubCategoryRepository.findById(subcategoryId).get();
    }


}
