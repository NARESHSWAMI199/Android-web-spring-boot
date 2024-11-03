package sales.application.sales.repostories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.ItemCategory;

@Repository
public interface ItemCategoryRepository extends JpaRepository<ItemCategory,Integer> {
}
