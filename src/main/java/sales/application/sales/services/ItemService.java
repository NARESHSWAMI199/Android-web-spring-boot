package sales.application.sales.services;


import static sales.application.sales.specifications.ItemSpecifications.containsName;
import static sales.application.sales.specifications.ItemSpecifications.isCategory;
import static sales.application.sales.specifications.ItemSpecifications.isCategoryName;
import static sales.application.sales.specifications.ItemSpecifications.isSubcategory;
import static sales.application.sales.specifications.ItemSpecifications.isSubcategoryName;
import static sales.application.sales.specifications.ItemSpecifications.isWholesaleId;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import sales.application.sales.dto.ItemDto;
import sales.application.sales.dto.RatingDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ItemService extends CommonRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemService.class);

    /** custom query */
    public Page<Map<String,Object>> getAllItems(SearchFilters searchFilters){
        logger.info("Received request to get all items with filters: {}", searchFilters);
        Pageable pageable = getPageable(searchFilters);
        String userZipCode = searchFilters.getZipCode();
        if (userZipCode == null || userZipCode.isEmpty()) {
            userZipCode = "0";
        }
        Page<Map<String, Object>> items = itemRepository.findAllItem(pageable, userZipCode);
        logger.info("Returning items with filters: {}", searchFilters);
        return items;
    }


    public Page<Item> getAllItem(ItemDto searchFilters) {
        logger.info("Received request to get all items with filters: {}", searchFilters);
        System.err.println( "The zip code : " + searchFilters.getZipCode());
        Specification<Item> specification = Specification.where(
                (containsName(searchFilters.getSearchKey().trim())
                        .or(isCategoryName(searchFilters.getSearchKey().trim()))
                        .or(isSubcategoryName(searchFilters.getSearchKey().trim())))
                        .and(isWholesaleId(searchFilters.getStoreId()))
                        .and(isCategory(searchFilters.getCategoryId()))
                        .and(isSubcategory(searchFilters.getSubcategoryId()))
        );
        Pageable pageable = getPageable(searchFilters);
        Page<Item> items = itemRepository.findAll(specification, pageable);

        if (searchFilters.getZipCode() != null && !searchFilters.getZipCode().isEmpty()) {
            List<Item> itemList = new ArrayList<>(items.getContent());
            itemList.sort(Comparator.comparing(item -> {
                Store store = storeRepository.findById(item.getWholesaleId()).orElse(null);
                if (store != null && store.getAddress() != null) {
                    return Math.abs(Integer.parseInt(store.getAddress().getZipCode()) - Integer.parseInt(searchFilters.getZipCode()));
                }
                return Integer.MAX_VALUE;
            }));
            logger.info("Items sorted by zip code: {}", searchFilters.getZipCode());
            Page<Item> sortedItems = new PageImpl<>(itemList, pageable, items.getTotalElements());
            logger.info("Returning sorted items with filters: {}", searchFilters);
            return sortedItems;
        }

        logger.info("Returning items with filters: {}", searchFilters);
        return items;
    }

    public List<ItemCategory> getAllItemCategories() {
        logger.info("Received request to get all item categories.");
        Sort sort =  Sort.by("id").descending();
        List<ItemCategory> categories = itemCategoryRepository.findAll(sort);
        logger.info("Returning all item categories.");
        return categories;
    }


    public Item findItemBySLug(String slug){
        logger.info("Received request to find item by slug: {}", slug);
        Item item = itemRepository.findItemBySlug(slug);
        logger.info("Returning item for slug: {}", slug);
        return item;
    }



    public List<ItemCategory> getAllItemsCategories(SearchFilters searchFilters) {
        logger.info("Received request to get all item categories with filters: {}", searchFilters);
        Sort sort = Sort.by(searchFilters.getOrderBy());
        sort = searchFilters.getOrder().equals("asc") ? sort.ascending():sort.descending() ;
        List<ItemCategory> categories = itemCategoryRepository.findAll(sort);
        logger.info("Returning all item categories with filters: {}", searchFilters);
        return categories;
    }

    public List<ItemSubCategory> getAllItemsSubCategories(SearchFilters searchFilters) {
        logger.info("Received request to get all item subcategories with filters: {}", searchFilters);
        Pageable pageable = getPageable(searchFilters);
        List<ItemSubCategory> subCategories;
        if(searchFilters.getCategoryId() != null){
            subCategories = itemSubCategoryRepository.getSubCategories(searchFilters.getCategoryId(),pageable);
        }else {
            subCategories = itemSubCategoryRepository.findAll(pageable).getContent();
        }
        logger.info("Returning all item subcategories with filters: {}", searchFilters);
        return subCategories;
    }



    @Transactional
    // handling here user's given ratings to items
    public int updateRatings(RatingDto ratingDto, User loggedUser){
        return storeHbRepository.updateStoreRatings(ratingDto,loggedUser);
    }

}
