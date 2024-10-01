package sales.application.sales.controllers;


import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import sales.application.sales.entities.User;

import java.util.HashMap;
import java.util.Map;


@RestController
public class UserController extends CommonService{



    @GetMapping("/auth/{slug}")
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

}
