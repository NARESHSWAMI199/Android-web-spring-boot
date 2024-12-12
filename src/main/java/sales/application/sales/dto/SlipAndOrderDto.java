package sales.application.sales.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import sales.application.sales.entities.Slip;

@Getter
@Setter
public class SlipAndOrderDto {
    int slipId = 0;
    int orderID = 0;
}
