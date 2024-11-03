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

    public static Specification<Item> isCategory(Integer categoryId ){
        if(categoryId == null || categoryId.equals(-1)) return null;
        return (root, query, builder) -> builder.equal(root.get(Item_.CATEGORY), categoryId);
    }

    public static Specification<Item> isSubcategory(Integer subcategoryId ){
        if(subcategoryId == null || subcategoryId.equals(-1)) return null;
        return (root, query, builder) -> builder.equal(root.get(Item_.SUBCATEGORY), subcategoryId);
    }
}
