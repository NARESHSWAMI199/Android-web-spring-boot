package sales.application.sales.repostories;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.dto.UserDto;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

@Component
@Transactional
public class ItemOrderHbRepository {
    @Autowired
    EntityManager entityManager;



    public int deleteItemOrder(Integer id){
        String hql = "Update ItemOrder set isDeleted='Y', updatedAt =:updatedAt where id=:id";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id",id);
        query.setParameter("updatedAt",Utils.getCurrentMillis());
        return query.executeUpdate();
    }

}
