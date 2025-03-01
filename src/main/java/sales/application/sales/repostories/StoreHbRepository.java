package sales.application.sales.repostories;



import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sales.application.sales.dto.RatingDto;
import sales.application.sales.dto.StoreDto;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreHbRepository {

    @Autowired
    EntityManager entityManager;

    public List<StoreDto> getAllStores(){
        String hql = "select s.name as storeName from Store s where s.id =:id";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id",1);
        StoreDto storeDto = (StoreDto) query.getSingleResult();
        List resultList = new ArrayList();
        resultList.add(storeDto);

        return resultList;
    }



    public int updateStoreRatings(RatingDto ratingDto, User loggedUser){
        Integer storeId = ratingDto.getStoreId();
        if(storeId == null || storeId == 0) throw new IllegalArgumentException("Store id can't be 0 or null.");
        String hql = "update StoreRating set rating=:rating,updatedAt=:updatedAt where userId=:userId and storeId=:storeId";
        Query updateStoreRatingQuery = entityManager.createQuery(hql);
        updateStoreRatingQuery.setParameter("rating",ratingDto.getRating());
        updateStoreRatingQuery.setParameter("storeId",storeId);
        updateStoreRatingQuery.setParameter("userId",loggedUser.getId());
        updateStoreRatingQuery.setParameter("updatedAt", Utils.getCurrentMillis());
        int isUpdated =  updateStoreRatingQuery.executeUpdate();
        if(isUpdated == 0) {
            String insertHql = """
            insert into StoreRating (storeId,userId,rating,createdAt,updatedAt) values( 
                :storeId,
                :userId,
                :rating,
                :createdAt,
                :updatedAt
            )
            """;
            Query insertStoreRatingQuery = entityManager.createQuery(insertHql);
            insertStoreRatingQuery.setParameter("storeId",storeId);
            insertStoreRatingQuery.setParameter("userId",loggedUser.getId());
            insertStoreRatingQuery.setParameter("rating",ratingDto.getRating());
            insertStoreRatingQuery.setParameter("createdAt", Utils.getCurrentMillis());
            insertStoreRatingQuery.setParameter("updatedAt", Utils.getCurrentMillis());
            insertStoreRatingQuery.executeUpdate();
        }
        Float ratingAvg = getRatingAvg(storeId);
        String storeHql = "update Store set rating=:ratingAvg where id=:storeId";
        Query storeUpdateQuery = entityManager.createQuery(storeHql);
        storeUpdateQuery.setParameter("storeId",storeId);
        storeUpdateQuery.setParameter("ratingAvg",ratingAvg);
        return storeUpdateQuery.executeUpdate();
    }


    public Float getRatingAvg(Integer storeId){
        String hql = """
            select 
                (sum(rating) / count(rating)) as rating_avg from StoreRating
            where storeId= :storeId 
            group by storeId
         """;
        Query query = entityManager.createQuery(hql);
        query.setParameter("storeId" , storeId);
        return ((Long) query.getSingleResult()).floatValue();
    }
}
