package sales.application.sales.repostories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.SlipItems;

import java.util.Map;

@Repository
public interface SlipItemsRepository extends JpaRepository<SlipItems,Integer>, JpaSpecificationExecutor<SlipItems> {
    Page<SlipItems> findDistinctBySlip(Slip slip , Pageable pageable);

    @Query(value = "from SlipItems where slip=:slip")
    Page<Map<String,Object>> getAllSlipItemBySlip(Slip slip , Pageable pageable);
}
