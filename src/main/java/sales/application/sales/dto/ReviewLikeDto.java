package sales.application.sales.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewLikeDto {

    Long commentId;
    Integer userId;
    Boolean isLiked;

}
