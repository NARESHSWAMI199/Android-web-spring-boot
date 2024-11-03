package sales.application.sales.entities;


import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Where;
import org.springframework.context.annotation.Lazy;
@Entity
@Table(name = "store")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "is_deleted != 'Y' and status != 'D'")
//@JsonInclude(JsonInclude.Include.ALWAYS)
public class Store {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            @Column(name = "id")
    int id;

    @Column(name = "slug")
    String slug;

    @Column(name = "name")
    String name;

    @Column(name = "avtar")
    String avatar;


    @Column(name = "category")
    Integer category;

    @Column(name = "subcategory")
    Integer subcategory;



    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address",nullable = true)
    Address address;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "discription")
    String description;

    @Column(name = "rating")
    Float rating;

    @Column(name = "status")
    String status;

    @Column(name = "is_deleted")
    String isDeleted;

    @Column(name = "created_at")
    Long createdAt;

    @Column(name = "updated_at")
    Long updatedAt;

    @Column(name = "created_by")
    Integer createdBy;

    @Column(name = "updated_by")
    Integer updatedBy;

//    @OneToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id",nullable = true)
//    User user;

}
