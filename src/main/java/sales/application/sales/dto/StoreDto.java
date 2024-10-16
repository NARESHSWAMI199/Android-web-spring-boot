package sales.application.sales.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
public class StoreDto extends ItemDto {

    /** store*/
    String storeSlug;
    String storeName;
    String storeAvtar;
    String Email;
    String phone;
    String discription;

    /** user */
    String userSlug;
    String username;
    String avtar;
    String userType;



}
