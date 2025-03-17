package sales.application.sales.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import sales.application.sales.entities.ItemReview;

@Data
@AllArgsConstructor
public class ReviewListDto {
    private String username;
    private ItemReview itemReview;
}
