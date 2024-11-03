package sales.application.sales.specifications;

import org.springframework.data.jpa.domain.Specification;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.Store;
import sales.application.sales.entities.Store_;

public class StoreSpecifications {



    public  static Specification<Store> containsName(String searchKey){
        if(searchKey == null) return null;
        return (root, query, builder) -> builder.like(root.get(Store_.NAME), "%"+searchKey+"%");
    }



    public static Specification<Store> isSubcategory(Integer subcategoryId ){
        if(subcategoryId == null || subcategoryId.equals(-1)) return null;
        return (root, query, builder) -> builder.equal(root.get(Store_.SUBCATEGORY), subcategoryId);
    }

    public static Specification<Store> isCategory(Integer categoryId ){
        if(categoryId == null || categoryId.equals(-1)) return null;
        return (root, query, builder) -> builder.equal(root.get(Store_.CATEGORY), categoryId);
    }


}
