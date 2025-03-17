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
import sales.application.sales.entities.ReviewDislikes;
import sales.application.sales.entities.ReviewLikes;
import sales.application.sales.entities.ItemReview;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

import java.util.List;

@Component
@Transactional
public class ItemReviewHbRepository {

    @Autowired
    EntityManager entityManager;


    public int updateReview(ItemReviewsDto itemReviewsDto,User loggedUser){
        String hql = """
            update ItemReview set 
                rating=:rating,
                message=:message,
                rating =:rating,
                updatedAt =:updatedAt 
            where userId=:userId and itemId=:itemId
            """;
        Query query = entityManager.createQuery(hql);
        query.setParameter("rating", itemReviewsDto.getRating());
        query.setParameter("message", itemReviewsDto.getMessage());
        query.setParameter("rating", itemReviewsDto.getRating());
        query.setParameter("updatedAt" , Utils.getCurrentMillis());
        query.setParameter("userId", loggedUser.getId());
        query.setParameter("itemId", itemReviewsDto.getItemId());
        return query.executeUpdate();
    }

    public Boolean isYouLiked(Long ItemReviewId, Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ReviewLikes> root = cq.from(ReviewLikes.class);

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


    public Boolean isYouDisliked(Long itemReviewId, Integer userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> cq = cb.createQuery(Long.class);
        Root<ReviewDislikes> root = cq.from(ReviewDislikes.class);

        cq.select(root.get("id"));
        cq.where(
                cb.and(
                        cb.equal(root.get("userId"), userId),
                        cb.equal(root.get("ItemReviewId"), itemReviewId)
                )
        );
        TypedQuery<Long> query = entityManager.createQuery(cq);
        List<Long> results = query.getResultList();
        return !results.isEmpty();
    }


    public void increaseReviewLikes(Long reviewId) {
        String hql = "update ItemReview set likes =likes+1 where id=:reviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("reviewId",reviewId);
        query.executeUpdate();
    }

    public void decreaseReviewLikes(Long reviewId) {
        String hql = "update ItemReview set likes=likes-1 where id=:reviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("reviewId",reviewId);
        query.executeUpdate();
    }



    public int likeReview(Long itemReviewId, Integer userId){
        String hql = "insert into ReviewLikes (`userId`,`ItemReviewId`)  values( :userId , :ItemReviewId )";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("ItemReviewId", itemReviewId);
        int isUpdated =   query.executeUpdate();
        if(isUpdated > 0) {
            increaseReviewLikes(itemReviewId);
        }
        return isUpdated;
    }

    public int removeLike(Long itemReviewId , Integer userId) {
        String hql = "delete from ReviewLikes where userId=:userId and ItemReviewId=:ItemReviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId",userId)
                .setParameter("ItemReviewId",itemReviewId);
        int update = query.executeUpdate();
        if(update > 0) {
            decreaseReviewLikes(itemReviewId);
        }
        return  update;
    }



    public void increaseReviewDislikes(Long reviewId) {
        String hql = "update ItemReview set dislikes=dislikes+1 where id=:reviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("reviewId",reviewId);
        query.executeUpdate();
    }

    public void decreaseReviewDislikes(Long reviewId) {
        String hql = "update ItemReview set dislikes=dislikes-1 where id=:reviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("reviewId",reviewId);
        query.executeUpdate();
    }


    public int dislikeReview(Long itemReviewId, Integer userId){
        String hql = "insert into ReviewDislikes (`userId`,`ItemReviewId`)  values( :userId , :ItemReviewId )";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId", userId);
        query.setParameter("ItemReviewId", itemReviewId);
        int isUpdated =  query.executeUpdate();
        if(isUpdated > 0) {
            increaseReviewDislikes(itemReviewId);
        }
        return isUpdated;
    }

    public int removeDislike(Long itemReviewId , Integer userId) {
        String hql = "delete from ReviewDislikes where userId=:userId and ItemReviewId=:ItemReviewId";
        Query query = entityManager.createQuery(hql);
        query.setParameter("userId",userId)
                .setParameter("ItemReviewId",itemReviewId);
        int update = query.executeUpdate();
        if(update > 0) {
            decreaseReviewDislikes(itemReviewId);
        }
        return  update;
    }


    public int deleteReview(String slug, User loggedUser){
        String hql = "update ItemReview set isDeleted='Y', updatedAt=:updatedAt where slug=:slug and user=:user";
        Query query = entityManager.createQuery(hql);
        query.setParameter("updatedAt",Utils.getCurrentMillis());
        query.setParameter("slug",slug);
        query.setParameter("user",loggedUser);
        return query.executeUpdate();
    }

}
