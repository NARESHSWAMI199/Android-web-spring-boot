package sales.application.sales.controllers;


import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import sales.application.sales.dto.ItemReviewsDto;
import sales.application.sales.entities.ItemReview;
import sales.application.sales.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("review")
public class ItemReviewController extends CommonService {

    private static final Logger logger = LoggerFactory.getLogger(ItemReviewController.class);

    @PostMapping("all")
    public ResponseEntity<Page<ItemReview>> getAllUsers(@RequestBody ItemReviewsDto ItemReviewsFilterDto, HttpServletRequest request) {
        logger.info("Received request to get all comments with filters: {}", ItemReviewsFilterDto);
        User loggedUser = null;
        try {
            loggedUser = itemReviewService.getUserByRequest(request);
        }catch (ExpiredJwtException ex){
            logger.info("session expired.");
        }
        Page<ItemReview> comments =  itemReviewService.getAllItemReview(ItemReviewsFilterDto,loggedUser);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }


    @GetMapping("detail/{slug}")
    public ResponseEntity<ItemReview> getDetailComment(@PathVariable String slug,HttpServletRequest request) {
        logger.info("Received request to get comment detail for slug: {}", slug);
        User loggedUser = null;
        try{
          loggedUser  = itemReviewService.getUserByRequest(request);
        }catch (ExpiredJwtException ex){
            logger.info("session expired.");
        }
        ItemReview ItemReview =  itemReviewService.findItemReviewBySlug(slug,loggedUser);
        return new ResponseEntity<>(ItemReview, HttpStatus.OK);
    }


    @PostMapping(value = {"add", "update"})
    public ResponseEntity<Map<String,Object>> updateOrSaveItem(@RequestBody ItemReviewsDto ItemReviewsDto, HttpServletRequest request) {
        logger.info("Received request to add/update comment: {}", ItemReviewsDto);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemReviewService.addOrUpdateComment(ItemReviewsDto,loggedUser);
        logger.info("Comment {} successfully.", ItemReviewsDto.getCommentSlug() == null ? "added" : "updated");
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @GetMapping("like/{reviewId}")
    public synchronized ResponseEntity<Map<String,Object>> addCommentLike(HttpServletRequest request , @PathVariable("reviewId") Long reviewId){
        logger.info("Received request to like review with ID: {}", reviewId);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemReviewService.addLikeReview(reviewId, loggedUser.getId());
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }


    @GetMapping("dislike/{commentId}")
    public synchronized ResponseEntity<Map<String,Object>> addCommentDislike(HttpServletRequest request , @PathVariable("commentId") Long commentId){
        logger.info("Received request to dislike comment with ID: {}", commentId);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemReviewService.addDislikeComment(commentId, loggedUser.getId());
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @PostMapping("delete/{commentSlug}")
    public ResponseEntity<Map<String,Object>> deleteCommentBySlug(HttpServletRequest request , @PathVariable("commentSlug") String commentSlug){
        logger.info("Received request to delete comment with slug: {}", commentSlug);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = new HashMap<>();
        int deleted = itemReviewService.deleteCommentBySlug(commentSlug, loggedUser);
        if(deleted > 0){
            responseObj.put("message","Message deleted successfully.");
            responseObj.put("status",200);
            logger.info("Comment with slug {} deleted successfully.", commentSlug);
        }else {
            responseObj.put("message","No record found.");
            responseObj.put("status",404);
            logger.info("No record found for comment with slug {}.", commentSlug);
        }
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }



}
