package sales.application.sales.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOrderDto {
    int orderId = 0;
    Integer itemId;
    Integer userId;
    Integer quantity;
    Integer slipId;
}
