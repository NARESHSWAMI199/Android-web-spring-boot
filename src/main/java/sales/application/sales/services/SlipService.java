package sales.application.sales.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sales.application.sales.controllers.CommonService;
import sales.application.sales.dto.SlipAndOrderDto;
import sales.application.sales.dto.SlipDto;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.User;
import sales.application.sales.exceptions.MyException;
import sales.application.sales.utilities.Utils;

import java.util.Optional;

@Service
public class SlipService extends CommonService {


    public Slip createNewSlip(SlipDto slipDto, User loggedUser){
        Slip slip = new Slip();
        slip.setSlipName(slipDto.getName());
        slip.setUserId(loggedUser.getId());
        slip.setCreatedAt(Utils.getCurrentMillis());
        slip.setUpdatedAt(Utils.getCurrentMillis());
        slip.setIsArchived("N");
        slip.setUpdatedBy(loggedUser.getId());
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

}
