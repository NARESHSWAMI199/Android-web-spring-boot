package sales.application.sales.services;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import sales.application.sales.dto.ItemOrderDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.SlipAndOrderDto;
import sales.application.sales.entities.*;
import sales.application.sales.exceptions.MyException;
import sales.application.sales.utilities.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ItemOrderService  extends CommonRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemOrderService.class);

    @Autowired
    SlipService slipService;


    public Page<SlipItems> getAllItemOrder(SearchFilters searchFilters,int slipId){
        logger.info("Received request to get all item orders for slip ID: {} with filters: {}", slipId, searchFilters);
        Pageable pageable = getPageable(searchFilters);
        return slipItemsRepository.findDistinctBySlipId(slipId, pageable);
    }

    @Transactional
    public Map<String,Object> saveNewOrder(ItemOrderDto itemOrderDto, User loggedUser){
        logger.info("Received request to save new order: {}", itemOrderDto);
        Map<String,Object> result = new HashMap<>();
        try {
            ItemOrder itemOrder = new ItemOrder();
            Optional<Item> item = itemRepository.findById(Long.valueOf(itemOrderDto.getItemId()));
            item.orElseThrow();
            itemOrder.setItem(item.get());
            itemOrder.setUserId(loggedUser.getId());
            Integer quantity = itemOrderDto.getQuantity();
            if (quantity == null || quantity == 0) {
                quantity = 1;
            }
            itemOrder.setQuantity(quantity);
            itemOrder.setCreatedAt(Utils.getCurrentMillis());
            itemOrder.setUpdatedAt(Utils.getCurrentMillis());
            itemOrder.setStatus("A");
            itemOrder.setIsDeleted("N");
            itemOrder.setUpdatedBy(loggedUser.getId());
            itemOrder = itemOrderRepository.save(itemOrder);
            logger.info("Order with ID {} saved successfully.", itemOrder.getId());



            Slip slip = slipService.findSlipById(itemOrderDto.getSlipId());
            /* Adding this order directly in slip */
            boolean orderedAddedInSlip = saveItemOrderInSlip(itemOrderDto.getSlipId(), itemOrder.getId());
            if(orderedAddedInSlip){
                result.put("message","Item successfully added in "+slip.getSlipName());
                result.put("status", 200);
                logger.info("Item successfully added in slip: {}", slip.getSlipName());
            }else {
                throw new MyException("We facing some issue during add item in "+slip);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();;
            logger.error("Error saving new order: {}", itemOrderDto, e);
            throw  e;
        }
        return result;
    }


    public boolean saveItemOrderInSlip(int slipId,int itemOrderId){
        logger.info("Saving item order ID {} in slip ID {}", itemOrderId, slipId);
        SlipAndOrderDto slipAndOrder = new SlipAndOrderDto();
        slipAndOrder.setOrderID(itemOrderId);
        slipAndOrder.setSlipId(slipId);
       int isUpdated = slipService.addNewItemInSlip(slipAndOrder);
       boolean result = isUpdated > 0;
       logger.info("Item order ID {} saved in slip ID {}: {}", itemOrderId, slipId, result);
       return result;
    }


}
