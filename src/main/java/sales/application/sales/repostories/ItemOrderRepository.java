package sales.application.sales.repostories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.ItemOrder;
import sales.application.sales.entities.Slip;

@Repository
public interface ItemOrderRepository extends JpaRepository<ItemOrder ,Integer> , JpaSpecificationExecutor<ItemOrder> {

}
