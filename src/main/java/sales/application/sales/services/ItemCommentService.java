package sales.application.sales.services;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.jetty.JettyWebServer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.ItemCommentsDto;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.ItemComment;
import sales.application.sales.entities.User;
import sales.application.sales.jwtUtils.JwtToken;
import sales.application.sales.utilities.Utils;

import static sales.application.sales.specifications.ItemCommentSpecification.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ItemCommentService extends CommonRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemCommentService.class);

    @Autowired
    JwtToken jwtToken;

    public Page<ItemComment> getAllItemComment(ItemCommentsDto filters, User loggedUser) {
        logger.info("Received request to get all item comments with filters: {}", filters);
        Map<String,Object> map = new HashMap<>();
        Specification<ItemComment> specification = Specification.where(
                (containsName(filters.getSearchKey()))
                        .and(hasSlug(filters.getCommentSlug()))
                        .and(isItemId(filters.getItemId()))
                        .and(isParentComment(filters.getParentId()))
        );
        Pageable pageable = getPageable(filters);
        Page<ItemComment> itemComments = itemCommentRepository.findAll(specification, pageable);
        List<ItemComment> content = itemComments.getContent();
        for (ItemComment comment : content) {
            comment.setRepliesCount(itemCommentRepository.totalReplies(comment.getId()));
            if (loggedUser != null) {
                comment.setIsLiked(itemCommentHbRepository.isYouLiked(comment.getId(), loggedUser.getId()));
                comment.setIsDisliked(itemCommentHbRepository.isYouDisliked(comment.getId(), loggedUser.getId()));
            }
        }
        return new PageImpl<>(content,pageable,itemComments.getTotalElements());
    }

    public ItemComment findItemCommentBySlug(String slug, User loggedUser) {
        logger.info("Received request to find item comment by slug: {}", slug);
        ItemComment itemComment = itemCommentRepository.findItemCommentBySlug(slug);
        if (loggedUser != null) {
            itemComment.setIsLiked(itemCommentHbRepository.isYouLiked(itemComment.getId(), loggedUser.getId()));
            itemComment.setRepliesCount(itemCommentRepository.totalReplies(itemComment.getId()));
        }
        return itemComment;
    }

    @Transactional
    public Map<String,Object> addOrUpdateComment(ItemCommentsDto itemCommentsDto, User loggedUser) {
        logger.info("Received request to add/update comment: {}", itemCommentsDto);
        Map<String,Object> responseObj = new HashMap<>();
        if (!Utils.isEmpty(itemCommentsDto.getCommentSlug())) {
            int isUpdated = itemCommentHbRepository.updateComment(itemCommentsDto);
            if (isUpdated > 0) {
                responseObj.put("message", "Comment successfully updated.");
                responseObj.put("status", 201);
                logger.info("Comment with slug {} updated successfully.", itemCommentsDto.getCommentSlug());
                return responseObj;
            }
        } else {
            ItemComment itemComment = new ItemComment();
            itemComment.setItemId(itemCommentsDto.getItemId());
            itemComment.setMessage(itemCommentsDto.getMessage());
            itemComment.setParentId(itemCommentsDto.getParentId());
            itemComment.setParentId(itemCommentsDto.getParentId());
            itemComment.setUser(loggedUser);
            itemComment.setCreatedAt(Utils.getCurrentMillis());
            itemComment.setUpdatedAt(Utils.getCurrentMillis());
            itemComment = itemCommentRepository.save(itemComment);
            if (itemComment.getId() > 0) {
                responseObj.put("res",itemComment);
                responseObj.put("message", "Comment successfully saved.");
                responseObj.put("status", 200);
                logger.info("Comment with ID {} saved successfully.", itemComment.getId());
                return responseObj;
            }
        }
        responseObj.put("message", "Facing some problem to save your comment.");
        responseObj.put("status", 400);
        logger.error("Error saving comment: {}", itemCommentsDto);
        return responseObj;
    }

    public Map<String,Object> addLikeComment(Long itemCommentId, Integer userId) {
        logger.info("Received request to like comment with ID: {}", itemCommentId);
        Map<String,Object> responseObj = new HashMap<>();
        int isLiked = 0;
        /* if user disliked then remove this dislikes also */
        Boolean alreadyDisliked = itemCommentHbRepository.isYouDisliked(itemCommentId, userId);
        if (alreadyDisliked) {
            int removed = itemCommentHbRepository.removeDislike(itemCommentId, userId);
            if (removed > 0) {
                responseObj.put("dislikes", -1);
            }
        }
        Boolean alreadyLiked = itemCommentHbRepository.isYouLiked(itemCommentId, userId);
        if (alreadyLiked) {
            int removed = itemCommentHbRepository.removeLike(itemCommentId, userId);
            if (removed > 0) {
                isLiked = -1;
            }
        } else {
            int liked = itemCommentHbRepository.likeComment(itemCommentId, userId);
            if (liked > 0) {
                isLiked = 1;
            }
        }
        responseObj.put("likes", isLiked);
        responseObj.put("isLiked", isLiked > 0);
        responseObj.put("isDisliked", false);
        responseObj.put("message", "Successfully updated");
        responseObj.put("status", 200);
        logger.info("Comment with ID {} liked status updated.", itemCommentId);
        return responseObj;
    }

    public Map<String,Object> addDislikeComment(Long itemCommentId, Integer userId) {
        logger.info("Received request to dislike comment with ID: {}", itemCommentId);
        Map<String,Object> responseObj = new HashMap<>();
        int isDisliked = 0;
        /* if user liked then remove this like also */
        Boolean alreadyLiked = itemCommentHbRepository.isYouLiked(itemCommentId, userId);
        if (alreadyLiked) {
            int removed = itemCommentHbRepository.removeLike(itemCommentId, userId);
            if (removed > 0) {
                responseObj.put("likes", -1);
            }
        }
        Boolean alreadyDisliked = itemCommentHbRepository.isYouDisliked(itemCommentId, userId);
        if (alreadyDisliked) {
            int removed = itemCommentHbRepository.removeDislike(itemCommentId, userId);
            if (removed > 0) {
                isDisliked = -1;
            }
        } else {
            int disliked = itemCommentHbRepository.dislikeComment(itemCommentId, userId);
            if (disliked > 0) {
                isDisliked = 1;
            }
        }
        responseObj.put("dislikes", isDisliked);
        responseObj.put("isDisliked", isDisliked > 0);
        responseObj.put("isLiked", false);
        responseObj.put("message", "Successfully updated");
        responseObj.put("status", 200);
        logger.info("Comment with ID {} disliked status updated.", itemCommentId);
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

    public int deleteCommentBySlug(String slug, User loggedUser) {
        logger.info("Received request to delete comment with slug: {}", slug);
        int deleted = itemCommentHbRepository.deleteComment(slug, loggedUser);
        logger.info("Comment with slug {} deleted by user ID {}.", slug, loggedUser.getId());
        return deleted;
    }
}
