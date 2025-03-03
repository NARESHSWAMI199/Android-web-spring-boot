package sales.application.sales.repostories;


import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.SlipItems;

import java.util.List;


@Repository
public interface SlipItemsRepository extends JpaRepository<SlipItems,Integer>, JpaSpecificationExecutor<SlipItems> {
    Page<SlipItems> findDistinctBySlipId(Integer slipId , Pageable pageable);
    List<SlipItems> findBySlipId(Integer slipId);

}
