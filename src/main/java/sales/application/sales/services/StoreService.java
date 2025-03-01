package sales.application.sales.services;


import static sales.application.sales.specifications.StoreSpecifications.isCategory;
import static sales.application.sales.specifications.StoreSpecifications.isCategoryName;
import static sales.application.sales.specifications.StoreSpecifications.isSubcategory;
import static sales.application.sales.specifications.StoreSpecifications.isSubcategoryName;

import java.util.List;
import java.util.Map;
import java.util.Comparator;
import java.util.ArrayList;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import sales.application.sales.dto.RatingDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Store;
import sales.application.sales.entities.StoreCategory;
import sales.application.sales.entities.StoreSubCategory;
import sales.application.sales.entities.User;
import sales.application.sales.specifications.StoreSpecifications;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class StoreService extends CommonRepository {

    private static final Logger logger = LoggerFactory.getLogger(StoreService.class);

    public Page<Map<String,Object>> getAllStores(SearchFilters searchFilters){
        logger.info("Received request to get all stores with filters: {}", searchFilters);
        Pageable pageable = getPageable(searchFilters);
        String userZipCode = searchFilters.getZipCode();
        if(userZipCode == null || userZipCode.isEmpty()){
            userZipCode = "0";
        }
        Page<Map<String, Object>> stores = storeRepository.findAllStore(pageable, searchFilters.getSearchKey(), userZipCode);
        logger.info("Returning all stores with filters: {}", searchFilters);
        return stores;
    }


    /** if you need more filters in future */
    public Page<Store> getAllStore(SearchFilters filters) {
        logger.info("Received request to get all stores with filters: {}", filters);
        Specification<Store> specification = Specification.where(
                (StoreSpecifications.containsName(filters.getSearchKey().trim())
                    .or(isCategoryName(filters.getSearchKey().trim()))
                    .or(isSubcategoryName(filters.getSearchKey().trim()))
                )
                .and(isCategory(filters.getCategoryId()))
                .and(isSubcategory(filters.getSubcategoryId()))
        );
        Pageable pageable = getPageable(filters);
        Page<Store> stores = storeRepository.findAll(specification, pageable);

        if (filters.getZipCode() != null && !filters.getZipCode().isEmpty()) {
            List<Store> storeList = new ArrayList<>(stores.getContent());
            storeList.sort(Comparator.comparing(store -> {
                if (store.getAddress() != null && store.getAddress().getZipCode() != null) {
                    return Math.abs(Integer.parseInt(store.getAddress().getZipCode()) - Integer.parseInt(filters.getZipCode()));
                }
                return Integer.MAX_VALUE;
            }));
            Page<Store> sortedStores = new PageImpl<>(storeList, pageable, stores.getTotalElements());
            logger.info("Returning sorted stores with filters: {}", filters);
            return sortedStores;
        }

        logger.info("Returning stores with filters: {}", filters);
        return stores;
    }

    public Store findStoreBySlug(String slug){
        logger.info("Received request to find store by slug: {}", slug);
        Store store = storeRepository.findStoreBySlug(slug);
        logger.info("Returning store for slug: {}", slug);
        return store;
    }

    public String getStoreNameById(Integer storeId){
        logger.info("Received request to get store name by ID: {}", storeId);
        String storeName = storeRepository.getStoreNameById(storeId);
        logger.info("Returning store name for ID: {}", storeId);
        return storeName;
    }


    public List<StoreCategory> getAllStoreCategories(SearchFilters searchFilters) {
        logger.info("Received request to get all store categories with filters: {}", searchFilters);
        Sort sort = Sort.by(searchFilters.getOrderBy());
           sort = searchFilters.getOrder().equals("asc") ? sort.ascending():sort.descending() ;
        List<StoreCategory> categories = storeCategoryRepository.findAll(sort);
        logger.info("Returning all store categories with filters: {}", searchFilters);
        return categories;
    }

    public List<StoreSubCategory> getAllStoreSubCategories(SearchFilters searchFilters) {
        logger.info("Received request to get all store subcategories with filters: {}", searchFilters);
        Pageable pageable = getPageable(searchFilters);
        List<StoreSubCategory> subCategories;
        if(searchFilters.getCategoryId() != null){
            subCategories = storeSubCategoryRepository.getSubCategories(searchFilters.getCategoryId(),pageable);
        }else {
            subCategories = storeSubCategoryRepository.findAll(pageable).getContent();
        }
        logger.info("Returning all store subcategories with filters: {}", searchFilters);
        return subCategories;
    }



    public StoreSubCategory getStoreSubcategoryDetail(Integer subcategoryId) {
        logger.info("Received request to get store subcategory detail for ID: {}", subcategoryId);
        StoreSubCategory storeSubCategory = storeSubCategoryRepository.findById(subcategoryId).orElse(null);
        logger.info("Returning store subcategory detail for ID: {}", subcategoryId);
        return storeSubCategory;
    }

    @Transactional
    // handling here user's given ratings to items
    public int updateRatings(RatingDto ratingDto, User loggedUser){
        return itemHbRepository.updateItemRatings(ratingDto,loggedUser);
    }


}
