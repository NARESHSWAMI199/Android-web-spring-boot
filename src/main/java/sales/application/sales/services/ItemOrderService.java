package sales.application.sales.services;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import sales.application.sales.controllers.CommonService;
import sales.application.sales.dto.ItemOrderDto;
import sales.application.sales.dto.SlipAndOrderDto;
import sales.application.sales.entities.ItemOrder;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.User;
import sales.application.sales.exceptions.MyException;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemOrderService  extends CommonService {


    @Autowired
    SlipService slipService;

    @Transactional
    public Map<String,Object> saveNewOrder(ItemOrderDto itemOrderDto, User loggedUser){
        Map<String,Object> result = new HashMap<>();
        try {
            ItemOrder itemOrder = new ItemOrder();
            itemOrder.setItemId(itemOrderDto.getItemId());
            itemOrder.setUserId(loggedUser.getId());
            Integer quantity = itemOrder.getQuantity();
            if (quantity == null || quantity == 0) {
                quantity = 1;
            }
            itemOrder.setQuantity(quantity);
            itemOrder = itemOrderRepository.save(itemOrder);


            /* Adding this order directly in slip */
            boolean orderedAddedInSlip = saveItemOrderInSlip(itemOrderDto.getSlipId(), itemOrder.getId());
            Slip slip = slipService.findSlipById(itemOrderDto.getSlipId());
            if(orderedAddedInSlip){
                result.put("message","Item successfully added in "+slip.getSlipName());
                result.put("status", 200);
            }else {
                throw new MyException("We facing some issue during add item in "+slip);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();;
            throw  e;
        }
        return result;
    }


    public boolean saveItemOrderInSlip(int itemOrderId , int slipId){
        SlipAndOrderDto slipAndOrder = new SlipAndOrderDto();
        slipAndOrder.setOrderID(itemOrderId);
        slipAndOrder.setSlipId(slipId);
       int isUpdated = slipService.addNewItemInSlip(slipAndOrder);
       return isUpdated > 0;
    }


}
