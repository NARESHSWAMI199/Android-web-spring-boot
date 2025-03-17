package sales.application.sales.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemReviewsDto extends SearchFilters{
    String message;
    Integer userId;
    Float rating;
    String userSlug;
    String itemSlug;
    String reviewSlug;
    long itemId;
    int parentId = 0;
}
