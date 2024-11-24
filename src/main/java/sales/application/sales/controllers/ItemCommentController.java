package sales.application.sales.controllers;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.CommentLikeDto;
import sales.application.sales.dto.ItemCommentsDto;
import sales.application.sales.entities.ItemComment;
import sales.application.sales.entities.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("comments")
public class ItemCommentController extends CommonService {

    @PostMapping("all")
    public ResponseEntity<List<ItemComment>> getAllUsers(@RequestBody ItemCommentsDto itemCommentsFilterDto, HttpServletRequest request) {
        User loggedUser = null;
        try {
            loggedUser = itemCommentService.getUserByRequest(request);
        }catch (ExpiredJwtException ex){
            logger.info("session expired.");
        }
        List<ItemComment> itemCommentsPage =  itemCommentService.getALlItemComment(itemCommentsFilterDto,loggedUser);
        return new ResponseEntity<>(itemCommentsPage, HttpStatus.OK);
    }


    @GetMapping("detail/{slug}")
    public ResponseEntity<ItemComment> getDetailComment(@PathVariable String slug,HttpServletRequest request) {
        User loggedUser = null;
        try{
          loggedUser  = itemCommentService.getUserByRequest(request);
        }catch (ExpiredJwtException ex){
            logger.info("session expired.");
        }
        ItemComment itemComment =  itemCommentService.findItemCommentBySlug(slug,loggedUser);
        return new ResponseEntity<>(itemComment, HttpStatus.OK);
    }


    @PostMapping(value = {"add", "update"})
    public ResponseEntity<Map<String,Object>> updateOrSaveItem(@RequestBody ItemCommentsDto itemCommentsDto, HttpServletRequest request) {
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemCommentService.addOrUpdateComment(itemCommentsDto,loggedUser);
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @GetMapping("like/{commentId}")
    public synchronized ResponseEntity<Map<String,Object>> addCommentLike(HttpServletRequest request , @PathVariable("commentId") Long commentId){
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemCommentService.addLikeComment(commentId, loggedUser.getId());
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }


    @GetMapping("dislike/{commentId}")
    public synchronized ResponseEntity<Map<String,Object>> addCommentDislike(HttpServletRequest request , @PathVariable("commentId") Long commentId){
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemCommentService.addDislikeComment(commentId, loggedUser.getId());
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }



}
