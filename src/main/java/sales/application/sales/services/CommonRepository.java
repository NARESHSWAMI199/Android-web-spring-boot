package sales.application.sales.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
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
    ItemHbRepository itemHbRepository;

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
    protected ItemReviewRepository itemReviewRepository;

    @Autowired
    protected ItemReviewHbRepository itemReviewHbRepository;

    @Autowired
    protected SlipRepository slipRepository;

    @Autowired
    protected SlipHbRepository slipHbRepository;

    @Autowired
    protected ItemOrderRepository itemOrderRepository;

    @Autowired
    protected SlipItemsRepository slipItemsRepository;

    @Autowired
    protected ItemOrderHbRepository itemOrderHbRepository;

    @Autowired
    protected SlipItemsHbRepository slipItemsHbRepository;

    @Autowired
    protected StoreRatingsRepository storeRatingsRepository;

    @Autowired
    protected ItemRatingsRepository itemRatingsRepository;


    public Pageable getPageable(SearchFilters searchFilters) {
        Sort sort = Sort.by(searchFilters.getOrderBy());
        sort = searchFilters.getOrder().equalsIgnoreCase("asc")  ? sort.ascending():sort.descending();
        return PageRequest.of(searchFilters.getPageNumber(),searchFilters.getPageSize(),sort);
    }

}
