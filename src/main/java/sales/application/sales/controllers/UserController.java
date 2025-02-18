package sales.application.sales.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sales.application.sales.dto.UserDto;
import sales.application.sales.entities.User;
import sales.application.sales.jwtUtils.JwtToken;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("auth")
public class UserController extends CommonService{

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    JwtToken jwtToken;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> findByEmailAndPassword(@RequestBody UserDto userDetails) {
        logger.info("Received login request with user details: {}", userDetails);
        Map<String, Object> responseObj = new HashMap<>();
        User user = userService.findByEmailAndPassword(userDetails);
        if (user == null) {
            responseObj.put("message", "invalid credentials.");
            responseObj.put("status", 401);
            logger.info("Invalid credentials for user: {}", userDetails.getEmail());
            return new ResponseEntity<>(responseObj, HttpStatus.UNAUTHORIZED);
        } else if (user.getStatus().equalsIgnoreCase("A")) {
            responseObj.put("token", "Bearer " + jwtToken.generateToken(user));
            responseObj.put("message", "success");
            responseObj.put("status", 200);
            responseObj.put("user", user);
            logger.info("User {} logged in successfully.", user.getEmail());
            return new ResponseEntity<>(responseObj, HttpStatus.OK);
        } else {
            responseObj.put("message", "You are blocked by admin");
            responseObj.put("status", 401);
            logger.info("User {} is blocked by admin.", user.getEmail());
            return new ResponseEntity<>(responseObj, HttpStatus.OK);
        }
    }



    @GetMapping("{slug}")
    public ResponseEntity<User> getUserBySlug(@PathVariable  String slug){
        logger.info("Received request to get user by slug: {}", slug);
        Map<String,Object> responseObj = new HashMap<>();
        try {
            User user = userService.findUserBySlug(slug);
            responseObj.put("data",user);
            responseObj.put("status",200);
            logger.info("User found for slug: {}", slug);
        }catch (Exception e ){
            responseObj.put("message",e.getMessage());
            responseObj.put("status",500);
            logger.error("Error finding user for slug: {}", slug, e);
        }
        return new ResponseEntity<User>(userService.findUserBySlug(slug), HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @Transactional
    @PostMapping(value = {"update"})
    public ResponseEntity<Map<String, Object>> updateAuth(HttpServletRequest request, @RequestBody UserDto userDto) {
        logger.info("Received request to update user: {}", userDto);
        Map<String,Object> responseObj = new HashMap<>();
        User logggedUser = (User) request.getAttribute("user");
        responseObj = userService.updateUserProfile(userDto, logggedUser);
        logger.info("User {} updated successfully.", userDto.getEmail());
        return new ResponseEntity<>(responseObj, HttpStatus.valueOf((Integer) responseObj.get("status")));

    }


    @Transactional
    @PostMapping(value = {"register"})
    public ResponseEntity<Map<String, Object>> register(HttpServletRequest request, @RequestBody UserDto userDto) throws Exception {
        logger.info("Received request to register user: {}", userDto);
        Map<String,Object> responseObj = userService.createUser(userDto);
        logger.info("User {} registered successfully.", userDto.getEmail());
        return new ResponseEntity<>(responseObj, HttpStatus.valueOf((Integer) responseObj.get("status")));
    }


    @Transactional
    @PostMapping("/update_profile/{slug}")
    public ResponseEntity<Map<String, Object>> updateProfileImage(HttpServletRequest request, @RequestPart MultipartFile profileImage, @PathVariable String slug ) {
        logger.info("Received request to update profile image for slug: {}", slug);
        Map<String,Object> responseObj = new HashMap<>();
        try {
            String  imageName = userService.updateProfileImage(profileImage,slug);
            if(imageName !=null) {
                responseObj.put("status" , 200);
                responseObj.put("imageName",imageName);
                responseObj.put("message" , "Profile image successfully updated");
                logger.info("Profile image for slug {} updated successfully.", slug);
            }else {
                responseObj.put("status" , 400);
                responseObj.put("message" , "Not a valid profile image");
                logger.info("Invalid profile image for slug {}.", slug);
            }
        } catch (Exception e) {
            responseObj.put("message", e.getMessage());
            responseObj.put("status", 500);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            logger.error("Error updating profile image for slug: {}", slug, e);
        }
        return new ResponseEntity<>(responseObj, HttpStatus.valueOf((Integer) responseObj.get("status")));

    }


    @Value("${profile.absolute}")
    String filePath;
    @GetMapping("/profile/{slug}/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable(required = true) String filename, @PathVariable("slug") String slug ) throws Exception {
        logger.info("Received request to get profile file: {}/{}", slug, filename);
        Path path = Paths.get(filePath +slug+ "/"+filename);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
    }


}
