package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "slip")
@Where(clause = "is_deleted != 'y'")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Slip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "slip_name")
    String slipName;
    @Column(name = "user_id")
    Integer userId;
    @Column(name = "is_archived")
    String isArchived;
    @Column(name = "is_deleted")
    String isDeleted = "N";
    @Column(name = "createdAt")
    Long createdAt;
    @Column(name = "updatedAt")
    Long updatedAt;
    @Column(name = "updated_by")
    Integer updatedBy;

}
