package sales.application.sales.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.ItemCategory;
import sales.application.sales.entities.ItemSubCategory;
import sales.application.sales.repostories.*;


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

    @Autowired
    protected UserHbRepository userHbRepository;
    @Autowired
    protected ItemSubCategoryRepository itemSubCategoryRepository;

    @Autowired
    protected ItemCategoryRepository itemCategoryRepository;

    @Autowired
    protected StoreCategoryRepository storeCategoryRepository;

    @Autowired
    protected StoreSubCategoryRepository storeSubCategoryRepository;

    @Autowired
    ItemCommentRepository itemCommentRepository;

    @Autowired
    ItemCommentHbRepository itemCommentHbRepository;


    public Pageable getPageable(SearchFilters searchFilters) {
        Sort sort = Sort.by(searchFilters.getOrderBy());
        sort = searchFilters.getOrder().equalsIgnoreCase("asc")  ? sort.ascending():sort.descending();
        return PageRequest.of(searchFilters.getPageNumber(),searchFilters.getPageSize(),sort);
    }

}
