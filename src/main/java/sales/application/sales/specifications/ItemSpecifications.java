package sales.application.sales.specifications;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;
import sales.application.sales.entities.*;

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
        return (root, query, builder) -> {
            Join<Item,ItemCategory> itemCategoryJoin = root.join("itemCategory",JoinType.INNER);
            return builder.equal(itemCategoryJoin.get("id"), categoryId);
        };
    }

    public static Specification<Item> isSubcategory(Integer subcategoryId ){
        if(subcategoryId == null || subcategoryId.equals(-1)) return null;
        return (root, query, builder) -> {
            Join<Item,ItemSubCategory> itemSubCategoryJoin = root.join("itemSubCategory",JoinType.INNER);
            return builder.equal(itemSubCategoryJoin.get("id"), subcategoryId);
        };
    }

    public static Specification<Item> isCategoryName(String category ){
        if(category == null || category.isEmpty()) return null;
        return (root, query, builder) -> {
            Join<Item,ItemCategory> itemCategoryJoin = root.join("itemCategory",JoinType.LEFT);
            return builder.like(itemCategoryJoin.get("category"), "%"+category+"%");
        };
    }


    public static Specification<Item> isSubcategoryName(String subcategory ){
        if(subcategory == null || subcategory.isEmpty()) return null;
        return (root, query, builder) -> {
            Join<Item,ItemCategory> itemCategoryJoin = root.join("itemSubCategory",JoinType.LEFT);
            return builder.like(itemCategoryJoin.get("subcategory"), "%"+subcategory+"%");
        };
    }




}
