package sales.application.sales.controllers;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sales.application.sales.dto.ItemCommentsDto;
import sales.application.sales.entities.ItemComments;
import sales.application.sales.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("comments")
public class ItemCommentController extends CommonService {

    @PostMapping("all")
    public ResponseEntity<List<ItemComments>> getAllUsers(@RequestBody ItemCommentsDto itemCommentsFilterDto, HttpServletRequest httpServletRequest) {
        List<ItemComments> itemCommentsPage =  itemCommentService.getALlItemComment(itemCommentsFilterDto);
        return new ResponseEntity<>(itemCommentsPage, HttpStatus.OK);
    }


    @PostMapping(value = {"add", "update"})
    public ResponseEntity<Map<String,Object>> updateOrSaveItem(@RequestBody ItemCommentsDto itemCommentsDto, HttpServletRequest request) {
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemCommentService.addOrUpdateComment(itemCommentsDto,loggedUser);
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }



}
