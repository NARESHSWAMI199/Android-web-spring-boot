package sales.application.sales.services;

import jakarta.persistence.Column;
import org.springframework.stereotype.Service;
import sales.application.sales.controllers.CommonService;
import sales.application.sales.dto.SlipDto;
import sales.application.sales.entities.Slip;

@Service
public class SlipService extends CommonService {


    public Slip createNewSlip(SlipDto slipDto){
        Slip slip = new Slip();
        slip.setSlipName(slipDto.getName());
        return slip;
    }


}
