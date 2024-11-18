package sales.application.sales.repostories;


import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import sales.application.sales.dto.ItemCommentsDto;
import sales.application.sales.utilities.Utils;

@Component
@Transactional
public class ItemCommentHbRepository {

    @Autowired
    EntityManager entityManager;


    public int updateComment(ItemCommentsDto itemCommentsDto){
        String hql = "update ItemComment set message=:message , updatedAt =:updatedAt where slug=:slug";
        Query query = entityManager.createQuery(hql);
        query.setParameter("message", itemCommentsDto.getMessage());
        query.setParameter("updatedAt" , Utils.getCurrentMillis());
        query.setParameter("slug", itemCommentsDto.getCommentSlug());
        return query.executeUpdate();
    }


}
