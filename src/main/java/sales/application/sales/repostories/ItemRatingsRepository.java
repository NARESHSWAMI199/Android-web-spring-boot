package sales.application.sales.repostories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.ItemRating;
import sales.application.sales.entities.StoreRating;

@Repository
public interface ItemRatingsRepository extends JpaRepository<ItemRating,Long> {
    ItemRating findByItemIdAndUserId(Integer itemId , Integer userId);
}
