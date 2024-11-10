package sales.application.sales.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.ItemDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.*;

import java.util.List;
import java.util.Map;

import static sales.application.sales.specifications.ItemSpecifications.*;


@Service
public class ItemService extends CommonRepository {

    /** custom query */
    public Page<Map<String,Object>> getAllItems(SearchFilters searchFilters){
        Pageable pageable = getPageable(searchFilters);
        return itemRepository.findALlItem(pageable);
    }


    public Page<Item> getAllItem(ItemDto searchFilters) {
        Specification<Item> specification = Specification.where(
                containsName(searchFilters.getSearchKey().trim())
                        .and(isWholesaleId(searchFilters.getStoreId()))
                        .and(isCategory(searchFilters.getCategoryId()))
                        .and(isSubcategory(searchFilters.getSubcategoryId()))
        );
        Pageable pageable = getPageable(searchFilters);
        return itemRepository.findAll(specification,pageable);
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
