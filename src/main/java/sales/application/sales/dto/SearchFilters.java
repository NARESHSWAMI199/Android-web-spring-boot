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
    String sortBy = "createdAt";
    String sort = "desc";
    Integer pageSize = 10;
    Integer pageNumber =0;


}
