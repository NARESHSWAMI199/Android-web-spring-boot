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
import sales.application.sales.dto.ItemCommentsDto;
import sales.application.sales.entities.CommentDislikes;
import sales.application.sales.entities.CommentLikes;
import sales.application.sales.entities.ItemComment;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

import java.util.List;

@Component
@Transactional
public class ItemCommentHbRepository {

    @Autowired
    EntityManager entityManager;


    public int updateComment(ItemCommentsDto itemCommentsDto){
        String hql = "update ItemComment set message=:message , updatedAt =:updatedAt where slug=:slug";
        Query query = entityManager.createQuery(hql);
        query.setParameter("message", itemCommentsDto.getMessage());
        query.setParameter("updatedAt" , Utils.getCurrentMillis());
        query.setParameter("slug", itemCommentsDto.getCommentSlug());
        return query.executeUpdate();
    }

    public Boolean isYouLiked(Long itemCommentId, Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CommentLikes> root = cq.from(CommentLikes.class);

        cq.select(root.get("id"));
        cq.where(
                cb.and(
                        cb.equal(root.get("userId"), userId),
                        cb.equal(root.get("itemCommentId"), itemCommentId)
                )
        );
        TypedQuery<Long> query = entityManager.createQuery(cq);
        List<Long> results = query.getResultList();
        return !results.isEmpty();
    }


    public Boolean isYouDisliked(Long itemCommentId, Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<CommentDislikes> root = cq.from(CommentDislikes.class);

        cq.select(root.get("id"));
        cq.where(
                cb.and(
                        cb.equal(root.get("userId"), userId),
                        cb.equal(root.get("itemCommentId"), itemCommentId)
                )
        );
        TypedQuery<Long> query = entityManager.createQuery(cq);
        List<Long> results = query.getResultList();
        return !results.isEmpty();
    }


    public void increaseCommentLikes(Long commentId) {
        String hql = "update ItemComment set likes =likes+1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }

    public void decreaseCommentLikes(Long commentId) {
        String hql = "update ItemComment set likes=likes-1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }



    public int likeComment(Long itemCommentId, Integer userId){
        String hql = "insert into CommentLikes (`userId`,`itemCommentId`)  values( :userId , :itemCommentId )";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("itemCommentId", itemCommentId);
        int isUpdated =   query.executeUpdate();
        if(isUpdated > 0) {
            increaseCommentLikes(itemCommentId);
        }
        return isUpdated;
    }

    public int removeLike(Long itemCommentId , Integer userId) {
        String hql = "delete from CommentLikes where userId=:userId and itemCommentId=:itemCommentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId",userId)
                .setParameter("itemCommentId",itemCommentId);
        int update = query.executeUpdate();
        if(update > 0) {
            decreaseCommentLikes(itemCommentId);
        }
        return  update;
    }



    public void increaseCommentDislikes(Long commentId) {
        String hql = "update ItemComment set dislikes=dislikes+1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }

    public void decreaseCommentDislikes(Long commentId) {
        String hql = "update ItemComment set dislikes=dislikes-1 where id=:commentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("commentId",commentId);
        query.executeUpdate();
    }


    public int dislikeComment(Long itemCommentId, Integer userId){
        String hql = "insert into CommentDislikes (`userId`,`itemCommentId`)  values( :userId , :itemCommentId )";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("itemCommentId", itemCommentId);
        int isUpdated =  query.executeUpdate();
        if(isUpdated > 0) {
            increaseCommentDislikes(itemCommentId);
        }
        return isUpdated;
    }

    public int removeDislike(Long itemCommentId , Integer userId) {
        String hql = "delete from CommentDislikes where userId=:userId and itemCommentId=:itemCommentId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId",userId)
                .setParameter("itemCommentId",itemCommentId);
        int update = query.executeUpdate();
        if(update > 0) {
            decreaseCommentDislikes(itemCommentId);
        }
        return  update;
    }


    public int deleteComment(String slug, User loggedUser){
        String hql = "update ItemComment set isDeleted='Y', updatedAt=:updatedAt where slug=:slug and user=:user";
        Query query = entityManager.createQuery(hql);
        query.setParameter("updatedAt",Utils.getCurrentMillis());
        query.setParameter("slug",slug);
        query.setParameter("user",loggedUser);
        return query.executeUpdate();
    }

}
