package sales.application.sales.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.Slips;

@Repository
public interface SlipsRepository extends JpaRepository<Slips,Integer> {
    
}
