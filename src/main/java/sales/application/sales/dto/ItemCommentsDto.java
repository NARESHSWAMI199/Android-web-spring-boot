package sales.application.sales.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ItemCommentsDto extends SearchFilters{
    String message;
    Integer userId;
    String userSlug;
    String itemSlug;
    String commentSlug;
    int itemId;
    int parentId = 0;
}
