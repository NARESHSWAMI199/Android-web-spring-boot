package sales.application.sales.dto;


import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemOrderDto {
    Integer itemId;;
    Integer quantity;
    Integer slipId;
}
