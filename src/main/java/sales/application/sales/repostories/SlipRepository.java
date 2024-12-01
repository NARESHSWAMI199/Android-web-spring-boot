package sales.application.sales.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.Slip;


@Repository
public interface SlipRepository extends JpaRepository<Slip,Integer> {
    
}
