package sales.application.sales.specifications;

import org.springframework.data.jpa.domain.Specification;
import sales.application.sales.entities.Store;
import sales.application.sales.entities.Store_;

public class StoreSpecifications {



    public  static Specification<Store> containsName(String searchKey){
        if(searchKey == null) return null;
        return (root, query, builder) -> builder.like(root.get(Store_.NAME), searchKey);
    }


}
