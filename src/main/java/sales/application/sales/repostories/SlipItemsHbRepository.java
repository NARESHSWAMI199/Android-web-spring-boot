package sales.application.sales.repostories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.utilities.Utils;

@Component
@Transactional
public class SlipItemsHbRepository {
    @Autowired
    EntityManager entityManager;



    public int deleteSlipItems(Integer id){
        String hql = "delete from SlipItems where id=:id";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id",id);
        return query.executeUpdate();
    }

}
