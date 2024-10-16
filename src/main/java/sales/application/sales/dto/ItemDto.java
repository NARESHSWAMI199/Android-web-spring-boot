package sales.application.sales.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemDto  extends SearchFilters{
    private String name;
    private Integer storeId;
    private Float price;
    private Float rating;
}
