package sales.application.sales.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;


@Table(name = "user")
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "user_id")
    int id ;

    @Column(name="slug")
    String slug;
    @Column(name="username")
    String username;
    @Column(name="email")
    String email;
    @Column(name="contact")
    String contact;
    @Column(name="password")
    String password;
    @Column(name="avtar")
    String avtar;
    @Column(name="user_type")
    String userType;
    @Column(name="status")
    String status;
    @Column(name="is_deleted")
    String isDeleted;
    @Column(name="created_at")
    Long createdAt;
    @Column(name="updated_at")
    Long updatedAt;
    @Column(name="created_by")
    Integer createdBy;
    @Column(name="updated_by")
    String updatedBy;

}
