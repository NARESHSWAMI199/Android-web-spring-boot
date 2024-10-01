package sales.application.sales.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Getter
@Setter
@Component
public class StoreDto {

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
