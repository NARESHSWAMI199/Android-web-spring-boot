package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "address")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    @Column(name = "slug")
    String slug;
    @Column(name = "city")
    Integer city;
    @Column(name="state")
    Integer state;
    @Column(name="latitude")
    Float latitude;
    @Column(name="altitude")
    Float altitude;
    @Column(name="created_at")
    Long createdAt;
    @Column(name="updated_at")
    Long updatedAt;
    @Column(name="created_by")
    Integer createdBy;
    @Column(name="updated_by")
    Integer updatedBy;


}
