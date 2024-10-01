package sales.application.sales.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.repostories.ItemRepository;
import sales.application.sales.repostories.StoreHbRepository;
import sales.application.sales.repostories.StoreRepository;
import sales.application.sales.repostories.UserRepository;


@Service
public class CommonRepository{

    @Autowired
    UserRepository userRepository;

    @Autowired
    StoreRepository storeRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    StoreHbRepository storeHbRepository;



    public Pageable getPageable(SearchFilters searchFilters) {
        Sort sort =  searchFilters.getSort().equalsIgnoreCase("asc")  ?  Sort.by(searchFilters.getSortBy()).ascending() :Sort.by(searchFilters.getSortBy()).descending();
        return PageRequest.of(searchFilters.getPageNumber(),searchFilters.getPageSize(),sort);
    }

}
