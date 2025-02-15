package sales.application.sales.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import sales.application.sales.utilities.Utils;

@Table(name = "item_order")
@Entity
@SQLRestriction("is_deleted != 'Y'")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ItemOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "item_id",referencedColumnName = "id")
    Item item;
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
