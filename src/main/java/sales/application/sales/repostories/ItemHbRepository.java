package sales.application.sales.repostories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.dto.RatingDto;
import sales.application.sales.entities.User;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Transactional
public class ItemHbRepository {
    @Autowired
    EntityManager entityManager;

    public int updateItemRatings(RatingDto ratingDto, User loggedUser){
        Integer itemId = ratingDto.getItemId();
        if(itemId == null || itemId == 0) throw new IllegalArgumentException("Item id can't be 0 or null");
        String hql = " update ItemRating set rating=:rating where userId=:userId and itemId=:itemId";
        Query updateItemRatingQuery = entityManager.createQuery(hql);
        updateItemRatingQuery.setParameter("rating",ratingDto.getRating());
        updateItemRatingQuery.setParameter("itemId",itemId);
        updateItemRatingQuery.setParameter("userId",loggedUser.getId());
        int isUpdated =  updateItemRatingQuery.executeUpdate();
        if(isUpdated == 0) {
            String insertHql = """
            insert into ItemRating (itemId,userId,rating) values( 
                :itemId,
                :userId,
                :rating
            )
            """;
            Query insertItemRatingQuery = entityManager.createQuery(insertHql);
            insertItemRatingQuery.setParameter("itemId",itemId);
            insertItemRatingQuery.setParameter("userId",loggedUser.getId());
            insertItemRatingQuery.setParameter("rating",ratingDto.getRating());
            insertItemRatingQuery.executeUpdate();
        }
        Float ratingAvg = getRatingAvg(itemId);
        String itemHql = "update Item set rating=:ratingAvg where id=:itemId";
        Query itemUpdateQuery = entityManager.createQuery(itemHql);
        itemUpdateQuery.setParameter("itemId",itemId);
        itemUpdateQuery.setParameter("ratingAvg",ratingAvg);
        return itemUpdateQuery.executeUpdate();
    }


    public Float getRatingAvg(Integer itemId){
        String hql = """
            select 
                (sum(rating) / count(rating)) as rating_avg from ItemRating
            where itemId= :itemId 
            group by itemId
         """;
        Query query = entityManager.createQuery(hql);
        query.setParameter("itemId" , itemId);
        return ((Long) query.getSingleResult()).floatValue();
    }

}
