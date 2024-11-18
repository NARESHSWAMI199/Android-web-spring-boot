package sales.application.sales.specifications;

import org.springframework.data.jpa.domain.Specification;
import sales.application.sales.entities.ItemComment;
import sales.application.sales.entities.ItemComment_;
import sales.application.sales.entities.User;

public class ItemCommentSpecification {


    public static Specification<ItemComment> containsName(String searchKey) {
        return (root, query, criteriaBuilder) -> {
            if (searchKey == null) return null;
            return criteriaBuilder.like(root.get(ItemComment_.MESSAGE), "%" + searchKey + "%");
        };
    }


    public static Specification<ItemComment> isItemId(int itemId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ItemComment_.ITEM_ID), itemId);
        };
    }

    public static Specification<ItemComment> isParentComment(int parentId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ItemComment_.PARENT_ID), parentId);
        };
    }



    public static Specification<ItemComment> greaterThanOrEqualFromDate(Long fromDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null) return  null;
            return criteriaBuilder.greaterThanOrEqualTo(root.get(ItemComment_.CREATED_AT), fromDate);
        };
    }

    public static Specification<ItemComment> lessThanOrEqualToToDate(Long toDate) {
        return (root, query, criteriaBuilder) -> {
            if (toDate == null) return null;
            return criteriaBuilder.lessThanOrEqualTo(root.get(ItemComment_.CREATED_AT), toDate);
        };
    }



    public static Specification<ItemComment> hasSlug(String slug) {
        return (root, query, criteriaBuilder) -> {
            if (slug == null || slug.isEmpty()) return null;
            return criteriaBuilder.equal(root.get(ItemComment_.SLUG), slug.trim());
        };
    }

    public static Specification<ItemComment> notSuperAdmin(User loggedUser) {
        return (root, query, criteriaBuilder) -> {
            if(loggedUser.getId() == 0) return  null;
            return criteriaBuilder.notEqual(root.get(ItemComment_.ID), 0);
        };
    }

    }


