package sales.application.sales.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter


public class SearchFilters {

    String searchKey="";
    String storeName;
    String username;
    String city;
    String state;
    String areaCode;
    String orderBy = "id";
    String order = "desc";
    Integer pageSize = 10;
    Integer pageNumber =0;
    Integer categoryId;
    Integer subcategoryId;
    String slipName;

}
