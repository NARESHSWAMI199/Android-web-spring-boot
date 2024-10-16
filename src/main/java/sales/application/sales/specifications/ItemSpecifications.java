package sales.application.sales.specifications;

import org.springframework.data.jpa.domain.Specification;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.Item_;

public class ItemSpecifications {


    public static Specification<Item> containsName(String searchKey){
        if(searchKey == null) return null;
        return (root, query, builder) -> builder.like(root.get(Item_.NAME), "%"+searchKey+"%");
    }


    public static Specification<Item> isWholesaleId(Integer storeId ){
        if(storeId == null) return null;
        return (root, query, builder) -> builder.equal(root.get(Item_.WHOLESALE_ID), storeId);
    }
}
