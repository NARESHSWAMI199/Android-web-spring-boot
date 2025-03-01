package sales.application.sales.repostories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import sales.application.sales.entities.ItemRating;
import sales.application.sales.entities.StoreRating;

@Repository
public interface StoreRatingsRepository extends JpaRepository<StoreRating,Long> {
    StoreRating findByStoreIdAndUserId(Integer storeId , Integer userId);
}
