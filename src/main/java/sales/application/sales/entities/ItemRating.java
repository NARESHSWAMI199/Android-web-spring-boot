package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item_ratings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id ;

    @Column(name = "item_id")
    Integer itemId;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "rating")
    Integer rating;



}
