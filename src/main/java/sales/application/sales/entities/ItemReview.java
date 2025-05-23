package sales.application.sales.entities;

import jakarta.persistence.*;
import jdk.jshell.execution.Util;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;
import sales.application.sales.dto.ItemReviewsDto;
import sales.application.sales.utilities.Utils;

import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_reviews")
@SQLRestriction(" is_deleted != 'Y'")
public class ItemReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "item_id",nullable = false)
    Long itemId;

    @Column(name = "rating")
    Float rating;

    @Column(name = "slug",nullable = false)
    String slug = UUID.randomUUID().toString();

    @Column(name = "user_id")
    Integer userId;

    @Column(name = "likes")
    Long likes=0L;

    @Column(name = "dislikes")
    Long dislikes=0L;

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

    /** this is dynamic according session user */
    @Transient
    Boolean isLiked;

    @Transient
    Boolean isDisliked;

}
