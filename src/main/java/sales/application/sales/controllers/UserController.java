package sales.application.sales.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.UserDto;
import sales.application.sales.entities.User;
import sales.application.sales.jwtUtils.JwtToken;

import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("auth")
public class UserController extends CommonService{


    @Autowired
    JwtToken jwtToken;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> findByEmailAndPassword(@RequestBody UserDto userDetails) {
        logger.info("====================== ADMIN LOGIN PROCESS STARTED ======================");
        Map<String, Object> responseObj = new HashMap<>();
        User user = userService.findByEmailAndPassword(userDetails);
        if (user == null) {
            responseObj.put("message", "invalid credentials.");
            responseObj.put("status", 401);
            return new ResponseEntity<>(responseObj, HttpStatus.UNAUTHORIZED);
        } else if (user.getStatus().equalsIgnoreCase("A")) {
            responseObj.put("token", "Bearer " + jwtToken.generateToken(user));
            responseObj.put("message", "success");
            responseObj.put("status", 200);
            responseObj.put("user", user);
            return new ResponseEntity<>(responseObj, HttpStatus.OK);
        } else {
            responseObj.put("message", "You are blocked by admin");
            responseObj.put("status", 401);
            return new ResponseEntity<>(responseObj, HttpStatus.OK);
        }
    }



    @GetMapping("{slug}")
    public ResponseEntity<User> getUserBySlug(@PathVariable  String slug){
        Map<String,Object> responseObj = new HashMap<>();
        try {
            User user = userService.findUserBySlug(slug);
            responseObj.put("data",user);
            responseObj.put("status",200);
        }catch (Exception e ){
            responseObj.put("message",e.getMessage());
            responseObj.put("status",500);
        }
        return new ResponseEntity<User>(userService.findUserBySlug(slug), HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @Transactional
    @PostMapping(value = {"update"})
    public ResponseEntity<Map<String, Object>> updateAuth(HttpServletRequest request, @RequestBody UserDto userDto) {
        Map<String,Object> responseObj = new HashMap<>();
        try {
            User logggedUser = (User) request.getAttribute("user");
            responseObj = userService.updateUserProfile(userDto, logggedUser);
        } catch (Exception e) {
            responseObj.put("message", e.getMessage());
            responseObj.put("status", 500);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return new ResponseEntity<>(responseObj, HttpStatus.valueOf((Integer) responseObj.get("status")));

    }

}
