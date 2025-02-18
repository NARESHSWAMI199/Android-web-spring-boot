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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.*;

@Service
public class UserService  extends CommonRepository{

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Value("${profile.absolute}")
    String profilePath;

    public User findUserBySlug(String slug){
        logger.info("Received request to find user by slug: {}", slug);
        User user = userRepository.findUserBySlug(slug);
        logger.info("Returning user for slug: {}", slug);
        return user;
    }


    @Transactional
    public Map<String, Object> updateUserProfile(UserDto userDto, User loggedUser){
        logger.info("Received request to update user profile: {}", userDto);
        String username = Utils.isValidName( userDto.getUsername());
        userDto.setUsername(username);
        Map<String, Object> responseObj = new HashMap<>();
        int isUpdated = updateUser(userDto, loggedUser);
        if (isUpdated > 0) {
            responseObj.put("message", "successfully updated.");
            responseObj.put("status", 201);
            logger.info("User profile updated successfully for user: {}", userDto.getEmail());
        } else {
            responseObj.put("message", "nothing to updated. may be something went wrong");
            responseObj.put("status", 400);
            logger.error("Error updating user profile for user: {}", userDto.getEmail());
        }
        return responseObj;
    }

    public User findByEmailAndPassword(UserDto userDto) {
        logger.info("Received request to find user by email and password: {}", userDto.getEmail());
        User user = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
        logger.info("Returning user for email: {}", userDto.getEmail());
        return user;
    }



    @Transactional
    public int updateUser(UserDto userDto, User loggedUser) {
        logger.info("Updating user: {}", userDto);
        int isUpdated = userHbRepository.updateUser(userDto, loggedUser);
        logger.info("User updated with ID: {}", userDto.getUserId());
        return isUpdated;
    }



    public List<ItemCategory> getAllItemCategories() {
        logger.info("Received request to get all item categories.");
        Sort sort =  Sort.by("id").descending();
        List<ItemCategory> categories = itemCategoryRepository.findAll(sort);
        logger.info("Returning all item categories.");
        return categories;
    }


    @Transactional
    public Map<String,Object>  createUser(UserDto userDto){
        logger.info("Received request to create user: {}", userDto);
        Map<String,Object> responseObj = new HashMap<>();
        String username = Utils.isValidName( userDto.getUsername());
        userDto.setUsername(username);
        User updatedUser = saveUser(userDto);
        userDto.setUserId(updatedUser.getId());
        if (updatedUser.getId() > 0) {
            responseObj.put("res", updatedUser);
            responseObj.put("message", "You have successfully registered.");
            responseObj.put("status", 200);
            logger.info("User registered successfully with ID: {}", updatedUser.getId());
        } else {
            responseObj.put("message", "nothing to save. may be something went wrong please contact to administrator.");
            responseObj.put("status", 400);
            logger.error("Error registering user: {}", userDto);
        }
        return responseObj;
    }


    @Transactional
    public User saveUser(UserDto userDto) {
        logger.info("Saving user: {}", userDto);
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setSlug(UUID.randomUUID().toString());
        user.setPassword(userDto.getPassword());
        user.setContact(userDto.getContact());
        user.setEmail(userDto.getEmail());
        user.setUserType("R");
        user.setPassword(userDto.getPassword());
        User savedUser = userRepository.save(user);
        logger.info("User saved with ID: {}", savedUser.getId());
        return savedUser;
    }

    @Transactional
    public String  updateProfileImage(MultipartFile profileImage, String slug) throws IOException {
        logger.info("Received request to update profile image for slug: {}", slug);
        String imageName = Objects.requireNonNull(profileImage.getOriginalFilename()).replaceAll(" ","_");
        if (!Utils.isValidImage(imageName)) {
            logger.error("Invalid profile image for slug: {}", slug);
            return null;
        }
        String dirPath = profilePath+slug+"/";
        File dir = new File(dirPath);
        if(!dir.exists()) dir.mkdirs();
        profileImage.transferTo(new File(dirPath+imageName));
        int isUpdated =  userHbRepository.updateProfileImage(slug,imageName);
        if(isUpdated > 0) {
            logger.info("Profile image updated successfully for slug: {}", slug);
            return imageName;
        }
        logger.error("Error updating profile image for slug: {}", slug);
        return null;
    }



}
