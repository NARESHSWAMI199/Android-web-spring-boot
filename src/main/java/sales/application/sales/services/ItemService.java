package sales.application.sales.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.ItemDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Item;

import java.util.Map;

import static sales.application.sales.specifications.ItemSpecifications.containsName;
import static sales.application.sales.specifications.ItemSpecifications.isWholesaleId;


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
        );
        Pageable pageable = getPageable(searchFilters);
        return itemRepository.findAll(specification,pageable);
    }


    public Item findItemBySLug(String slug){
        return itemRepository.findItemBySlug(slug);
    }
}
