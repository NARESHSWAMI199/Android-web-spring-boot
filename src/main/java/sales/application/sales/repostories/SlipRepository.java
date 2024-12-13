package sales.application.sales.repostories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.Slip;


@Repository
public interface SlipRepository extends JpaRepository<Slip,Integer> , JpaSpecificationExecutor<Slip> {

    Page<Slip> findAllByUserId(Integer userId, Pageable pageable);

}
