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
import sales.application.sales.entities.ItemOrder;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.SlipItems;
import sales.application.sales.entities.User;
import sales.application.sales.exceptions.MyException;
import sales.application.sales.utilities.Utils;

import java.util.HashMap;
import java.util.Map;

@Service
public class ItemOrderService  extends CommonRepository {


    @Autowired
    SlipService slipService;


    public Page<SlipItems> getAllItemOrder(SearchFilters searchFilters,int slipId){
        Pageable pageable = getPageable(searchFilters);
        Slip slip = slipService.findSlipById(slipId);
        return  slipItemsRepository.findDistinctBySlip(slip,pageable);
    }

    @Transactional
    public Map<String,Object> saveNewOrder(ItemOrderDto itemOrderDto, User loggedUser){
        Map<String,Object> result = new HashMap<>();
        try {
            ItemOrder itemOrder = new ItemOrder();
            itemOrder.setItemId(itemOrderDto.getItemId());
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



            Slip slip = slipService.findSlipById(itemOrderDto.getSlipId());
            /* Adding this order directly in slip */
            boolean orderedAddedInSlip = saveItemOrderInSlip(itemOrderDto.getSlipId(), itemOrder.getId());
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


    public boolean saveItemOrderInSlip(int slipId,int itemOrderId){
        SlipAndOrderDto slipAndOrder = new SlipAndOrderDto();
        slipAndOrder.setOrderID(itemOrderId);
        slipAndOrder.setSlipId(slipId);
       int isUpdated = slipService.addNewItemInSlip(slipAndOrder);
       return isUpdated > 0;
    }


}
