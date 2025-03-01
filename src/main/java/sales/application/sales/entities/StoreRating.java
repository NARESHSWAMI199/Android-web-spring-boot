package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "store_ratings")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StoreRating {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id ;

    @Column(name = "store_id")
    Integer storeId;

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "rating")
    Integer rating;

    @Column(name = "created_at")
    Long createdAt;

    @Column(name = "updated_at")
    Long updatedAt;



}
