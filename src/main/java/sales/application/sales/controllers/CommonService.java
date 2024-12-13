package sales.application.sales.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.repostories.ItemOrderRepository;
import sales.application.sales.repostories.SlipHbRepository;
import sales.application.sales.repostories.SlipRepository;
import sales.application.sales.services.*;

import java.util.logging.Logger;

@Component
public class CommonService {

    @Autowired
    protected UserService userService;

    @Autowired
    protected StoreService storeService;

    @Autowired
    protected ItemService itemService;

    @Autowired
    protected Logger logger;

    @Autowired
    protected ItemCommentService itemCommentService;

    @Autowired
    protected SlipService slipService;

    @Autowired
    protected  ItemOrderService itemOrderService;

}
