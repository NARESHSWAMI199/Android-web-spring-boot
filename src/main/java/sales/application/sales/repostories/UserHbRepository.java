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
public class UserHbRepository {
    @Autowired
    EntityManager entityManager;
    public int updateUser(UserDto userDto,User loggedUser){
        String strQuery = "update User set " +
                "username=:username , " +
                "email=:email,"+
                "contact=:contact,"+
                "updatedAt=:updatedAt,"+
                "updatedBy=:updatedBy "+
                "where slug =:slug";

        Query query = entityManager.createQuery(strQuery);
        query.setParameter("username", userDto.getUsername());
        query.setParameter("email", userDto.getEmail());
        query.setParameter("contact", userDto.getContact());
        query.setParameter("updatedAt", Utils.getCurrentMillis());
        query.setParameter("updatedBy", loggedUser.getId());
        query.setParameter("slug", userDto.getSlug());
        return query.executeUpdate();
    }
}
