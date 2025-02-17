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

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import sales.application.sales.dto.ItemDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.ItemCategory;
import sales.application.sales.entities.ItemSubCategory;
import sales.application.sales.entities.Store;


@Service
public class ItemService extends CommonRepository {

    /** custom query */
    public Page<Map<String,Object>> getAllItems(SearchFilters searchFilters){
        Pageable pageable = getPageable(searchFilters);
        String userZipCode = searchFilters.getZipCode();
        if (userZipCode == null || userZipCode.isEmpty()) {
            userZipCode = "0";
        }
        return itemRepository.findAllItem(pageable, userZipCode);
    }


    public Page<Item> getAllItem(ItemDto searchFilters) {
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
            return new PageImpl<>(itemList, pageable, items.getTotalElements());
        }

        return items;
    }

    public List<ItemCategory> getAllItemCategories() {
        Sort sort =  Sort.by("id").descending();
        return itemCategoryRepository.findAll(sort);
    }


    public Item findItemBySLug(String slug){
        return itemRepository.findItemBySlug(slug);
    }



    public List<ItemCategory> getAllItemsCategories(SearchFilters searchFilters) {
        Sort sort = Sort.by(searchFilters.getOrderBy());
        sort = searchFilters.getOrder().equals("asc") ? sort.ascending():sort.descending() ;
        return itemCategoryRepository.findAll(sort);
    }

    public List<ItemSubCategory> getAllItemsSubCategories(SearchFilters searchFilters) {
        Pageable pageable = getPageable(searchFilters);
        if(searchFilters.getCategoryId() != null){
            return itemSubCategoryRepository.getSubCategories(searchFilters.getCategoryId(),pageable);
        }else {
            return itemSubCategoryRepository.findAll(pageable).getContent();
        }
    }

}
