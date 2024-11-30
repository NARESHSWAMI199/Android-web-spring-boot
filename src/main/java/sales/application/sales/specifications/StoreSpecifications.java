package sales.application.sales.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import sales.application.sales.entities.*;

public class StoreSpecifications {



    public  static Specification<Store> containsName(String searchKey){
        if(searchKey == null) return null;
        return (root, query, builder) -> builder.like(root.get(Store_.NAME), "%"+searchKey+"%");
    }

    public static Specification<Store> isSubcategory(Integer subcategoryId ){
        if(subcategoryId == null || subcategoryId.equals(-1)) return null;
        return (root, query, builder) -> {
            Join<Store, StoreSubCategory> storeSubCategoryJoin = root.join("storeSubCategory", JoinType.INNER);
            return builder.equal(storeSubCategoryJoin.get("id"), subcategoryId);
        };
    }

    public static Specification<Store> isCategory(Integer categoryId ) {
        if (categoryId == null || categoryId.equals(-1)) return null;
        return (root, query, builder) -> {
            Join<Store, StoreCategory> storeSubCategoryJoin = root.join("storeCategory", JoinType.INNER);
            return builder.equal(storeSubCategoryJoin.get("id"), categoryId);
        };
    }

    public static Specification<Store> isCategoryName(String category ){
        if(category == null || category.isEmpty()) return null;
        return (root, query, builder) -> {
            Join<Item,ItemCategory> itemCategoryJoin = root.join("storeCategory",JoinType.LEFT);
            return builder.like(itemCategoryJoin.get("category"), "%"+category+"%");
        };
    }


    public static Specification<Store> isSubcategoryName(String subcategory ){
        if(subcategory == null || subcategory.isEmpty()) return null;
        return (root, query, builder) -> {
            Join<Item,ItemCategory> itemCategoryJoin = root.join("storeSubCategory",JoinType.LEFT);
            return builder.like(itemCategoryJoin.get("subcategory"), "%"+subcategory+"%");
        };
    }

}
