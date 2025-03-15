package sales.application.sales.repostories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.dto.ItemReviewsDto;
import sales.application.sales.entities.CommentDislikes;
import sales.application.sales.entities.CommentLikes;
import sales.application.sales.entities.ItemReview;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

import java.util.List;

@Component
@Transactional
public class ItemReviewHbRepository {

    @Autowired
    EntityManager entityManager;


    public int updateComment(ItemReviewsDto ItemReviewsDto){
        String hql = "update ItemReview set message=:message , updatedAt =:updatedAt where slug=:slug";
        Query query = entityManager.createQuery(hql);
        query.setParameter("message", ItemReviewsDto.getMessage());
        query.setParameter("updatedAt" , Utils.getCurrentMillis());
        query.setParameter("slug", ItemReviewsDto.getCommentSlug());
        return query.executeUpdate();
    }

    public Boolean isYouLiked(Long ItemReviewId, Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CommentLikes> root = cq.from(CommentLikes.class);

        cq.select(root.get("id"));
        cq.where(
                cb.and(
                        cb.equal(root.get("userId"), userId),
                        cb.equal(root.get("ItemReviewId"), ItemReviewId)
                )
        );
        TypedQuery<Long> query = entityManager.createQuery(cq);
        List<Long> results = query.getResultList();
        return !results.isEmpty();
    }


    public Boolean isYouDisliked(Long ItemReviewId, Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CommentDislikes> root = cq.from(CommentDislikes.class);

        cq.select(root.get("id"));
        cq.where(
                cb.and(
                        cb.equal(root.get("userId"), userId),
                        cb.equal(root.get("ItemReviewId"), ItemReviewId)
                )
        );
        TypedQuery<Long> query = entityManager.createQuery(cq);
        List<Long> results = query.getResultList();
        return !results.isEmpty();
    }


    public void increaseCommentLikes(Long commentId) {
        String hql = "update ItemReview set likes =likes+1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }

    public void decreaseCommentLikes(Long commentId) {
        String hql = "update ItemReview set likes=likes-1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }



    public int likeComment(Long ItemReviewId, Integer userId){
        String hql = "insert into CommentLikes (`userId`,`ItemReviewId`)  values( :userId , :ItemReviewId )";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("ItemReviewId", ItemReviewId);
        int isUpdated =   query.executeUpdate();
        if(isUpdated > 0) {
            increaseCommentLikes(ItemReviewId);
        }
        return isUpdated;
    }

    public int removeLike(Long ItemReviewId , Integer userId) {
        String hql = "delete from CommentLikes where userId=:userId and ItemReviewId=:ItemReviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId",userId)
                .setParameter("ItemReviewId",ItemReviewId);
        int update = query.executeUpdate();
        if(update > 0) {
            decreaseCommentLikes(ItemReviewId);
        }
        return  update;
    }



    public void increaseCommentDislikes(Long commentId) {
        String hql = "update ItemReview set dislikes=dislikes+1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }

    public void decreaseCommentDislikes(Long commentId) {
        String hql = "update ItemReview set dislikes=dislikes-1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }


    public int dislikeComment(Long ItemReviewId, Integer userId){
        String hql = "insert into CommentDislikes (`userId`,`ItemReviewId`)  values( :userId , :ItemReviewId )";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("ItemReviewId", ItemReviewId);
        int isUpdated =  query.executeUpdate();
        if(isUpdated > 0) {
            increaseCommentDislikes(ItemReviewId);
        }
        return isUpdated;
    }

    public int removeDislike(Long ItemReviewId , Integer userId) {
        String hql = "delete from CommentDislikes where userId=:userId and ItemReviewId=:ItemReviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId",userId)
                .setParameter("ItemReviewId",ItemReviewId);
        int update = query.executeUpdate();
        if(update > 0) {
            decreaseCommentDislikes(ItemReviewId);
        }
        return  update;
    }


    public int deleteComment(String slug, User loggedUser){
        String hql = "update ItemReview set isDeleted='Y', updatedAt=:updatedAt where slug=:slug and user=:user";
        Query query = entityManager.createQuery(hql);
        query.setParameter("updatedAt",Utils.getCurrentMillis());
        query.setParameter("slug",slug);
        query.setParameter("user",loggedUser);
        return query.executeUpdate();
    }

}
