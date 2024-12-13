package sales.application.sales.repostories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.dto.SlipAndOrderDto;
import sales.application.sales.dto.SlipDto;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

import javax.swing.text.html.parser.Entity;

@Component
@Transactional
public class SlipHbRepository {


    @Autowired
    EntityManager entityManager;

    public int updateSlip(SlipDto slipDto, User loggedUser){
        String hql = "update Slip set slipName=:slipName ,updatedAt=:updatedAt, updatedBy=:updatedBy where id=:id" ;
        Query query = entityManager.createQuery(hql);
        query.setParameter("slipName",slipDto.getName());
        query.setParameter("updatedAt", Utils.getCurrentMillis());
        query.setParameter("updatedBy",loggedUser.getId());
        query.setParameter("id",slipDto.getId());
        return query.executeUpdate();
    }

    public int insertNewOrderInSlip(int slipId, int orderId){
        String sql = "insert into slip_items (`slip_id`,`item_order_id`) value(:slipId, :itemOrderId)";
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.setParameter("slipId",slipId);
        nativeQuery.setParameter("itemOrderId",orderId);
        return nativeQuery.executeUpdate();
    }

}
