package sales.application.sales.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.SlipAndOrderDto;
import sales.application.sales.dto.SlipDto;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.User;
import sales.application.sales.exceptions.MyException;
import sales.application.sales.utilities.Utils;

import java.util.HashMap;
import java.util.Map;


@Service
public class SlipService extends CommonRepository {



    public Page<Slip> getAllActiveSlips(SearchFilters  searchFilters,User loggeduser) {
        Pageable pageable = getPageable(searchFilters);
        return slipRepository.findAllByUserId(loggeduser.getId(),pageable);
    }



    public Map<String,Object> createOrUpdate(SlipDto slipDto,User loggedUser){
        Map<String,Object> result = new HashMap<>();
        if(slipDto.getId() < 1){
            Slip slip = createNewSlip(slipDto,loggedUser);
            if(slip.getId() > 0){
                result.put("res",slip);
                result.put("message", "Slip successfully created.");
                result.put("status", 201);
            }else {
                result.put("message", "Something went wrong during insert.");
                result.put("status", 400);
            }
        }else {
            int isUpdated = updateSlip(slipDto, loggedUser);
            if(isUpdated > 0){
                result.put("message", "Slip successfully updated.");
                result.put("status", 200);
            }else {
                result.put("message", "Something went wrong during update.");
                result.put("status", 200);
            }
        }
        return  result;
    }

    public Slip createNewSlip(SlipDto slipDto, User loggedUser){
        if(Utils.isEmpty(slipDto.getName())) throw new MyException("Slip name can't be blank.");
        Slip slip = Slip.builder()
            .slipName(slipDto.getName())
            .userId(loggedUser.getId())
            .createdAt(Utils.getCurrentMillis())
            .updatedAt(Utils.getCurrentMillis())
            .isArchived("N")
            .isDeleted("N")
            .updatedBy(loggedUser.getId())
            .build();
        return slipRepository.save(slip);
    }

    @Transactional
    public int updateSlip(SlipDto slipDto, User loggedUser){
       return slipHbRepository.updateSlip(slipDto,loggedUser);
    }


    public Slip findSlipById(Integer id){
        Slip slip =  slipRepository.findById(id).orElse(null);
        if(slip == null){
            throw new MyException("Slip id doesn't exists");
        }
        return slip;
    }

    @Transactional
    public int addNewItemInSlip(SlipAndOrderDto slipAndOrder){
        int slipId = slipAndOrder.getSlipId();
        int orderId = slipAndOrder.getOrderID();
        if(slipId != 0 && orderId != 0){
           return slipHbRepository.insertNewOrderInSlip(slipId,orderId);
        }
        return 0;
    }

    public int removeSlipItems(Integer id){
        return slipItemsHbRepository.deleteSlipItems(id);
    }



    public int deleteSlip(Map<String,Object> params,User loggedUser) {
        Integer slipId = (Integer) params.get("id");
        return  slipHbRepository.deleteSlip(slipId,loggedUser.getId());
    }

}
