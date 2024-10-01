package sales.application.sales.services;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.Store;

import java.util.Map;


@Service
public class ItemService extends CommonRepository {

    public Page<Map<String,Object>> getAllItem(SearchFilters searchFilters){
        Pageable pageable = getPageable(searchFilters);
        return itemRepository.findALlItem(pageable);
    }

    public Item findItemBySLug(String slug){
        return itemRepository.findItemBySlug(slug);
    }
}
