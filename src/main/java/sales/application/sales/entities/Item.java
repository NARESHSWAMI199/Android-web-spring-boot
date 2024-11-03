package sales.application.sales.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serializable;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "item")
@Where(clause = "is_deleted != 'Y' and  status !='D' ")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;
    @Column(name = "name")
    String name;

    @Column(name = "label")
    String label;

    @Column(name = "price")
    float price;
    @Column(name = "discount")
    float discount;

    @Column(name = "category")
    Integer category;

    @Column(name = "subcategory")
    Integer subcategory;

    @Column(name = "description")
    String description;
    @Column(name = "avatar")
    String avatar;
    @Column(name = "rating")
    float rating;
    @JsonIgnore
    @Column(name = "status")
    String status="A";
    @JsonIgnore
    @Column(name = "is_deleted")
    String isDeleted="N";
    @Column(name = "created_at")
    @JsonIgnore
    Long createdAt;
    @Column(name = "created_by")
    @JsonIgnore
    Integer createdBy;
    @Column(name = "updated_at")
    @JsonIgnore
    Long updatedAt;
    @Column(name = "updated_by")
    @JsonIgnore
    Integer updatedBy;
    @Column(name = "slug")
    String slug;
    @Column(name = "in_stock")
    String inStock;

    @Column(name = "wholesale_id")
    Integer wholesaleId;

    @Transient
    Store store;

}