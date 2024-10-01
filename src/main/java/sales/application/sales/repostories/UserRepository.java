package sales.application.sales.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long>{


    /*** find user by slug */
   User findUserBySlug(String slug);

}
