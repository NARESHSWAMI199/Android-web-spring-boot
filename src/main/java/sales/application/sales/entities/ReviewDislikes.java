package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "comment_dislikes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDislikes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "item_comment_id")
    Integer ItemReviewId;

}
