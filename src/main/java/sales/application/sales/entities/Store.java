package sales.application.sales.entities;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
@Entity
@Table(name = "stores")

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SQLRestriction("is_deleted != 'Y' and status != 'D'")
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

    @Column(name = "avatar")
    String avatar;


    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    StoreCategory storeCategory;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subcategory_id", referencedColumnName = "id")
    StoreSubCategory storeSubCategory;



    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "address_id",nullable = true)
    Address address;

    @Column(name = "email")
    String email;

    @Column(name = "phone")
    String phone;

    @Column(name = "description")
    String description;

    @Column(name = "rating")
    Float rating;

    @Column(name = "status")
    String status;

    @JsonIgnore
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


}
