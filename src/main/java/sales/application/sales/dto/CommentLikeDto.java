package sales.application.sales.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentLikeDto {

    Long commentId;
    Integer userId;
    Boolean isLiked;

}
