package sales.application.sales.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.ItemOrder;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder ,Integer> {
}
