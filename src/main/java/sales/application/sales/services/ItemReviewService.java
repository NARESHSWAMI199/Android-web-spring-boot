package sales.application.sales.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sales.application.sales.dto.ItemReviewsDto;
import sales.application.sales.dto.ReviewListDto;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.ItemReview;
import sales.application.sales.entities.User;
import sales.application.sales.jwtUtils.JwtToken;
import sales.application.sales.utilities.Utils;

import static sales.application.sales.specifications.ItemReviewSpecification.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ItemReviewService extends CommonRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemReviewService.class);

    @Autowired
    JwtToken jwtToken;

    public Page<ReviewListDto> getAllItemReview(ItemReviewsDto filters, User loggedUser) {
        logger.info("Received request to get all item reviews with filters: {}", filters);
        Map<String,Object> map = new HashMap<>();
        Pageable pageable = getPageable(filters);
        Page<ReviewListDto> ItemReviews = itemReviewRepository.getAllReviewByItemId(filters.getItemId(),pageable);
        List<ReviewListDto> content = ItemReviews.getContent();
        for (ReviewListDto reviewListDto : content) {
            ItemReview review = reviewListDto.getItemReview();
            review.setRepliesCount(itemReviewRepository.totalReplies(review.getId()));
            if (loggedUser != null) {
                review.setIsLiked(itemReviewHbRepository.isYouLiked(review.getId(), loggedUser.getId()));
                review.setIsDisliked(itemReviewHbRepository.isYouDisliked(review.getId(), loggedUser.getId()));
            }
        }
        return new PageImpl<>(content,pageable,ItemReviews.getTotalElements());
    }

    public ItemReview findItemReviewBySlug(String slug, User loggedUser) {
        logger.info("Received request to find item review by slug: {}", slug);
        ItemReview ItemReview = itemReviewRepository.findItemReviewBySlug(slug);
        if (loggedUser != null) {
            ItemReview.setIsLiked(itemReviewHbRepository.isYouLiked(ItemReview.getId(), loggedUser.getId()));
            ItemReview.setRepliesCount(itemReviewRepository.totalReplies(ItemReview.getId()));
        }
        return ItemReview;
    }

    @Transactional
    public Map<String,Object> addOrUpdateReview(ItemReviewsDto itemReviewsDto, User loggedUser) {
        logger.info("Received request to add/update review: {}", itemReviewsDto);
        Map<String,Object> responseObj = new HashMap<>();
            int isUpdated = itemReviewHbRepository.updateReview(itemReviewsDto,loggedUser);
            if (isUpdated > 0) {
                responseObj.put("message", "Review successfully updated.");
                responseObj.put("status", 201);
                logger.info("Review with slug {} updated successfully.", itemReviewsDto.getReviewSlug());
                return responseObj;
            }

            // else we're going to add this review
        ItemReview itemReview = getItemReview(itemReviewsDto, loggedUser);
        itemReview = itemReviewRepository.save(itemReview);
            responseObj.put("res",itemReview);
            responseObj.put("message", "Review successfully saved.");
            responseObj.put("status", 200);
            logger.info("Review with ID {} saved successfully.", itemReview.getId());
            return responseObj;
}

    private static ItemReview getItemReview(ItemReviewsDto itemReviewsDto, User loggedUser) {
        ItemReview itemReview = new ItemReview();
        itemReview.setItemId(itemReviewsDto.getItemId());
        itemReview.setMessage(itemReviewsDto.getMessage());
        itemReview.setParentId(itemReviewsDto.getParentId());
        itemReview.setParentId(itemReviewsDto.getParentId());
        itemReview.setUserId(loggedUser.getId());
        itemReview.setCreatedAt(Utils.getCurrentMillis());
        itemReview.setUpdatedAt(Utils.getCurrentMillis());
        itemReview.setRating(itemReviewsDto.getRating());
        return itemReview;
    }


    @Transactional(isolation = Isolation.SERIALIZABLE,propagation= Propagation.REQUIRES_NEW)
    public Map<String,Object> addLikeReview(Long itemReviewId, Integer userId) {
        logger.info("Received request to like review with ID: {}", itemReviewId);
        Map<String,Object> responseObj = new HashMap<>();
        int isLiked = 0;
        /* if user disliked then remove this dislikes also */
        Boolean alreadyDisliked = itemReviewHbRepository.isYouDisliked(itemReviewId, userId);
        if (alreadyDisliked) {
            int removed = itemReviewHbRepository.removeDislike(itemReviewId, userId);
            if (removed > 0) {
                responseObj.put("dislikes", -1);
            }
        }
        Boolean alreadyLiked = itemReviewHbRepository.isYouLiked(itemReviewId, userId);
        if (alreadyLiked) {
            int removed = itemReviewHbRepository.removeLike(itemReviewId, userId);
            if (removed > 0) {
                isLiked = -1;
            }
        } else {
            int liked = itemReviewHbRepository.likeReview(itemReviewId, userId);
            if (liked > 0) {
                isLiked = 1;
            }
        }
        responseObj.put("likes", isLiked);
        responseObj.put("isLiked", isLiked > 0);
        responseObj.put("isDisliked", false);
        responseObj.put("message", "Successfully updated");
        responseObj.put("status", 200);
        logger.info("Review with ID {} liked status updated.", itemReviewId);
        return responseObj;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE,propagation= Propagation.REQUIRES_NEW)
    public Map<String,Object> addDislikeReview(Long itemReviewId, Integer userId) {
        logger.info("Received request to dislike review with ID: {}", itemReviewId);
        Map<String,Object> responseObj = new HashMap<>();
        int isDisliked = 0;
        /* if user liked then remove this like also */
        Boolean alreadyLiked = itemReviewHbRepository.isYouLiked(itemReviewId, userId);
        if (alreadyLiked) {
            int removed = itemReviewHbRepository.removeLike(itemReviewId, userId);
            if (removed > 0) {
                responseObj.put("likes", -1);
            }
        }
        Boolean alreadyDisliked = itemReviewHbRepository.isYouDisliked(itemReviewId, userId);
        if (alreadyDisliked) {
            int removed = itemReviewHbRepository.removeDislike(itemReviewId, userId);
            if (removed > 0) {
                isDisliked = -1;
            }
        } else {
            int disliked = itemReviewHbRepository.dislikeReview(itemReviewId, userId);
            if (disliked > 0) {
                isDisliked = 1;
            }
        }
        responseObj.put("dislikes", isDisliked);
        responseObj.put("isDisliked", isDisliked > 0);
        responseObj.put("isLiked", false);
        responseObj.put("message", "Successfully updated");
        responseObj.put("status", 200);
        logger.info("Review with ID {} disliked status updated.", itemReviewId);
        return responseObj;
    }

    public User getUserByRequest(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        logger.info("Received request with token: {}", token);
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String slug = jwtToken.getSlugFromToken(token);
            User user = userRepository.findUserBySlug(slug);
            request.setAttribute("user", user);
            logger.info("User found for token: {}", token);
            return user;
        }
        return null;
    }

    public int deleteReviewBySlug(String slug, User loggedUser) {
        logger.info("Received request to delete review with slug: {}", slug);
        int deleted = itemReviewHbRepository.deleteReview(slug, loggedUser);
        logger.info("Review with slug {} deleted by user ID {}.", slug, loggedUser.getId());
        return deleted;
    }
}
