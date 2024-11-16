package sales.application.sales.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User,Long>{



    @Query(value = "from User where email=:email and password=:password and userType='R' ")
    User findByEmailAndPassword(@Param("email") String email, @Param("password") String password);



    /*** find user by slug */
   User findUserBySlug(String slug);

}
