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
public class ItemComments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Column(name = "item_id")
    Integer itemId;

    @Column(name = "slug")
    String slug = UUID.randomUUID().toString();

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "parent_id")
    Integer parentId;

    @Column(name = "message")
    String message;

    @Column(name = "created_at")
    Long createdAt;

    @Column(name="is_deleted")
    String isDeleted ="N";

    @Column(name = "updated_at")
    Long updatedAt;

    @Transient
    Integer repliesCount;



    public ItemComments (User loggedUser) {
        this.createdAt = Utils.getCurrentMillis();
        this.updatedAt = Utils.getCurrentMillis();
        this.userId = loggedUser.getId();
    }


}
