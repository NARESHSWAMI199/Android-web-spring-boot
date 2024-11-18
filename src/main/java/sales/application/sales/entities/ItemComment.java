package sales.application.sales.entities;

import jakarta.persistence.*;
import jdk.jshell.execution.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Where;
import sales.application.sales.dto.ItemCommentsDto;
import sales.application.sales.utilities.Utils;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_comments")
@Where(clause = " is_deleted != 'Y'")
public class ItemComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "item_id",nullable = false)
    Integer itemId;

    @Column(name = "slug",nullable = false)
    String slug = UUID.randomUUID().toString();

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    User user;

    @Column(name = "parent_id",nullable = false)
    Integer parentId;

    @Column(name = "message",nullable = false)
    String message;

    @Column(name = "created_at",nullable = false)
    Long createdAt;

    @Column(name="is_deleted")
    String isDeleted ="N";

    @Column(name = "updated_at")
    Long updatedAt;

    @Transient
    Integer repliesCount;


}
