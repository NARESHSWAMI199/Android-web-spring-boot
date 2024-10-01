package sales.application.sales.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sales.application.sales.entities.User;
import sales.application.sales.repostories.UserRepository;

@Service
public class UserService  extends CommonRepository{

    public User findUserBySlug(String slug){
        return userRepository.findUserBySlug(slug);
    }

}
