package sales.application.sales.repostories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.ItemCategory;
import sales.application.sales.entities.StoreCategory;

@Repository
public interface StoreCategoryRepository extends JpaRepository<StoreCategory,Integer> {
}
