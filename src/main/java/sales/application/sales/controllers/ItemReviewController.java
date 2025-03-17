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
import sales.application.sales.dto.ReviewListDto;
import sales.application.sales.entities.ItemReview;
import sales.application.sales.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("review")
public class ItemReviewController extends CommonService {

    private static final Logger logger = LoggerFactory.getLogger(ItemReviewController.class);

    @PostMapping("all")
    public ResponseEntity<Page<ReviewListDto>> getAllUsers(@RequestBody ItemReviewsDto itemReviewsFilterDto, HttpServletRequest request) {
        logger.info("Received request to get all reviews with filters: {}", itemReviewsFilterDto);
        User loggedUser = null;
        try {
            loggedUser = itemReviewService.getUserByRequest(request);
        }catch (ExpiredJwtException ex){
            logger.info("session expired during getting all reviews for  : {} .",itemReviewsFilterDto.getItemId());
        }
        Page<ReviewListDto> reviews =  itemReviewService.getAllItemReview(itemReviewsFilterDto,loggedUser);
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }


    @GetMapping("detail/{slug}")
    public ResponseEntity<ItemReview> getDetailReview(@PathVariable String slug,HttpServletRequest request) {
        logger.info("Received request to get review detail for slug: {}", slug);
        User loggedUser = null;
        try{
          loggedUser  = itemReviewService.getUserByRequest(request);
        }catch (ExpiredJwtException ex){
            logger.info("session expired during getting review detail by slug.");
        }
        ItemReview ItemReview =  itemReviewService.findItemReviewBySlug(slug,loggedUser);
        return new ResponseEntity<>(ItemReview, HttpStatus.OK);
    }


    @PostMapping(value = {"add", "update"})
    public ResponseEntity<Map<String,Object>> updateOrSaveItem(@RequestBody ItemReviewsDto itemReviewsDto, HttpServletRequest request) {
        logger.info("Received request to add/update review: {}", itemReviewsDto);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemReviewService.addOrUpdateReview(itemReviewsDto,loggedUser);
        logger.info("Review {} successfully.", itemReviewsDto.getReviewSlug() == null ? "added" : "updated");
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @GetMapping("like/{reviewId}")
    public synchronized ResponseEntity<Map<String,Object>> addReviewLike(HttpServletRequest request , @PathVariable("reviewId") Long reviewId){
        logger.info("Received request to like review with ID: {}", reviewId);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemReviewService.addLikeReview(reviewId, loggedUser.getId());
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }


    @GetMapping("dislike/{reviewId}")
    public synchronized ResponseEntity<Map<String,Object>> addReviewDislike(HttpServletRequest request , @PathVariable("reviewId") Long reviewId){
        logger.info("Received request to dislike review with ID: {}", reviewId);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = itemReviewService.addDislikeReview(reviewId, loggedUser.getId());
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @PostMapping("delete/{reviewSlug}")
    public ResponseEntity<Map<String,Object>> deleteReviewBySlug(HttpServletRequest request , @PathVariable("reviewSlug") String reviewSlug){
        logger.info("Received request to delete review with slug: {}", reviewSlug);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = new HashMap<>();
        int deleted = itemReviewService.deleteReviewBySlug(reviewSlug, loggedUser);
        if(deleted > 0){
            responseObj.put("message","Message deleted successfully.");
            responseObj.put("status",200);
            logger.info("Review with slug {} deleted successfully.", reviewSlug);
        }else {
            responseObj.put("message","No record found.");
            responseObj.put("status",404);
            logger.info("No record found for review with slug {}.", reviewSlug);
        }
        return new ResponseEntity<>(responseObj,HttpStatus.valueOf((Integer) responseObj.get("status")));
    }



}
