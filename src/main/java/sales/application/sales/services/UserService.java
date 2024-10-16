package sales.application.sales.services;


import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.UserDto;
import sales.application.sales.entities.User;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService  extends CommonRepository{

    public User findUserBySlug(String slug){
        return userRepository.findUserBySlug(slug);
    }


    @Transactional
    public Map<String, Object> updateUserProfile(UserDto userDto, User loggedUser){
        Map<String, Object> responseObj = new HashMap<>();
        int isUpdated = updateUser(userDto, loggedUser);
        if (isUpdated > 0) {
            responseObj.put("message", "successfully updated.");
            responseObj.put("status", 201);
        } else {
            responseObj.put("message", "nothing to updated. may be something went wrong");
            responseObj.put("status", 400);
        }
        return responseObj;
    }



    @Transactional
    public int updateUser(UserDto userDto, User loggedUser) {
        return userHbRepository.updateUser(userDto, loggedUser);
    }





}
