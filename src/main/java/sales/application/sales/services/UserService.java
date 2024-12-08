package sales.application.sales.services;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import sales.application.sales.dto.UserDto;
import sales.application.sales.entities.ItemCategory;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService  extends CommonRepository{


    @Value("${profile.absolute}")
    String profilePath;

    public User findUserBySlug(String slug){
        return userRepository.findUserBySlug(slug);
    }


    @Transactional
    public Map<String, Object> updateUserProfile(UserDto userDto, User loggedUser){
        String username = Utils.isValidName( userDto.getUsername());
        userDto.setUsername(username);
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

    public User findByEmailAndPassword(UserDto userDto) {
        return userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
    }



    @Transactional
    public int updateUser(UserDto userDto, User loggedUser) {
        return userHbRepository.updateUser(userDto, loggedUser);
    }



    public List<ItemCategory> getAllItemCategories() {
        Sort sort =  Sort.by("id").descending();
        return itemCategoryRepository.findAll(sort);
    }


    @Transactional
    public Map<String,Object>  createUser(UserDto userDto){
        Map<String,Object> responseObj = new HashMap<>();
        String username = Utils.isValidName( userDto.getUsername());
        userDto.setUsername(username);
        User updatedUser = saveUser(userDto);
        userDto.setUserId(updatedUser.getId());
        if (updatedUser.getId() > 0) {
            responseObj.put("res", updatedUser);
            responseObj.put("message", "You have successfully registered.");
            responseObj.put("status", 200);
        } else {
            responseObj.put("message", "nothing to save. may be something went wrong please contact to administrator.");
            responseObj.put("status", 400);
        }
        return responseObj;
    }


    @Transactional
    public User saveUser(UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setSlug(UUID.randomUUID().toString());
        user.setPassword(userDto.getPassword());
        user.setContact(userDto.getContact());
        user.setEmail(userDto.getEmail());
        user.setUserType("R");
        user.setPassword(userDto.getPassword());
        return userRepository.save(user);
    }

    @Transactional
    public String  updateProfileImage(MultipartFile profileImage, String slug) throws IOException {
        String imageName = Objects.requireNonNull(profileImage.getOriginalFilename()).replaceAll(" ","_");
        if (!Utils.isValidImage(imageName)) return null;
        String dirPath = profilePath+slug+"/";
        File dir = new File(dirPath);
        if(!dir.exists()) dir.mkdirs();
        profileImage.transferTo(new File(dirPath+imageName));
        int isUpdated =  userHbRepository.updateProfileImage(slug,imageName);
        if(isUpdated > 0) return imageName;
        return null;
    }



}
