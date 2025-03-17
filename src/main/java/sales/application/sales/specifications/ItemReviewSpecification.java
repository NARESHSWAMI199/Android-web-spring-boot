package sales.application.sales.specifications;

import org.springframework.data.jpa.domain.Specification;
import sales.application.sales.entities.ItemReview;
import sales.application.sales.entities.ItemReview_;
import sales.application.sales.entities.User;

public class ItemReviewSpecification {


    public static Specification<ItemReview> containsName(String searchKey) {
        return (root, query, criteriaBuilder) -> {
            if (searchKey == null) return null;
            return criteriaBuilder.like(root.get(ItemReview_.MESSAGE), "%" + searchKey + "%");
        };
    }


    public static Specification<ItemReview> isItemId(int itemId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ItemReview_.ITEM_ID), itemId);
        };
    }

    public static Specification<ItemReview> isParentReview(int parentId) {
        return (root, query, criteriaBuilder) -> {
            return criteriaBuilder.equal(root.get(ItemReview_.PARENT_ID), parentId);
        };
    }



    public static Specification<ItemReview> greaterThanOrEqualFromDate(Long fromDate) {
        return (root, query, criteriaBuilder) -> {
            if (fromDate == null) return  null;
            return criteriaBuilder.greaterThanOrEqualTo(root.get(ItemReview_.CREATED_AT), fromDate);
        };
    }

    public static Specification<ItemReview> lessThanOrEqualToToDate(Long toDate) {
        return (root, query, criteriaBuilder) -> {
            if (toDate == null) return null;
            return criteriaBuilder.lessThanOrEqualTo(root.get(ItemReview_.CREATED_AT), toDate);
        };
    }



    public static Specification<ItemReview> hasSlug(String slug) {
        return (root, query, criteriaBuilder) -> {
            if (slug == null || slug.isEmpty()) return null;
            return criteriaBuilder.equal(root.get(ItemReview_.SLUG), slug.trim());
        };
    }

    public static Specification<ItemReview> notSuperAdmin(User loggedUser) {
        return (root, query, criteriaBuilder) -> {
            if(loggedUser.getId() == 0) return  null;
            return criteriaBuilder.notEqual(root.get(ItemReview_.ID), 0);
        };
    }

    }


