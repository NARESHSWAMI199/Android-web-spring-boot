package sales.application.sales.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import sales.application.sales.utilities.Utils;

@Table(name = "item_order")
@Entity
@Where(clause = "is_deleted != 'y'")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "item_id")
    Integer itemId;
    @Column(name = "user_id")
    Integer userId;
    @Column(name = "quantity")
    Integer quantity;
    @Column(name = "status")
    String status;
    @Column(name = "is_deleted")
    String isDeleted = "N";
    @Column(name = "createdAt")
    Long createdAt=Utils.getCurrentMillis();
    @Column(name = "updatedAt")
    Long updatedAt = Utils.getCurrentMillis();
    @Column(name = "updated_by")
    Integer updatedBy;

}
