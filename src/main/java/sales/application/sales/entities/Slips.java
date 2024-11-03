package sales.application.sales.entities;


import jakarta.persistence.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "slips")
@Where(clause = "is_deleted != 'y'")
public class Slips {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "slug")
    String  slug;
    @Column(name = "item_id")
    String  itemId;
    @Column(name = "quantity")
    Float quantity;
    @Column(name = "wholesaleId")
    Integer wholesaleId;
    @Column(name = "status")
    String status;
    @Column(name = "is_deleted")
    String isDeleted;
    @Column(name = "createdAt")
    Long createdAt;
    @Column(name = "updatedAt")
    Long updatedAt;
    @Column(name = "updated_by")
    Integer updatedBy;
    @Column(name="created_by")
    Integer createdBy;

}
