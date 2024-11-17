package sales.application.sales.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Columns;
import sales.application.sales.utilities.Utils;

import java.util.UUID;


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
    String slug = UUID.randomUUID().toString();
    @Column(name="username",nullable = false)
    String username;
    @Column(name="email",nullable = false)
    String email;
    @Column(name="contact",nullable = false)
    String contact;
    @Column(name="password",nullable = false)
    @JsonIgnore
    String password;
    @Column(name="avtar")
    String avtar;
    @Column(name="user_type",nullable = false)
    String userType = "R";
    @Column(name="status")
    String status="A";
    @JsonIgnore
    @Column(name="is_deleted")
    String isDeleted="N";
    @Column(name="created_at",nullable = false)
    Long createdAt=Utils.getCurrentMillis();
    @Column(name="updated_at")
    Long updatedAt;
    @Column(name="created_by")
    Integer createdBy;
    @Column(name="updated_by")
    String updatedBy;


}
