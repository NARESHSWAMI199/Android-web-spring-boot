package sales.application.sales.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.services.ItemService;
import sales.application.sales.services.StoreService;
import sales.application.sales.services.UserService;

import java.util.logging.Logger;

@Component
public class CommonService {

    @Autowired
    UserService userService;

    @Autowired
    StoreService storeService;

    @Autowired
    ItemService itemService;

    @Autowired
    Logger logger;

}
