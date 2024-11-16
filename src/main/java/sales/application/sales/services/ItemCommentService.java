package sales.application.sales.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.ItemCommentsDto;
import sales.application.sales.entities.ItemComments;
import sales.application.sales.entities.User;
import sales.application.sales.utilities.Utils;

import static sales.application.sales.specifications.ItemCommentSpecification.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemCommentService extends  CommonRepository {

    public List<ItemComments> getALlItemComment(ItemCommentsDto filters) {
        Specification<ItemComments> specification = Specification.where(
                (containsName(filters.getSearchKey()))
                        .and(hasSlug(filters.getCommentSlug()))
                        .and(isItemId(filters.getItemId()))
                        .and(isParentComment(filters.getParentId()))
        );
        Pageable pageable = getPageable(filters);
        Page<ItemComments> itemComments = itemCommentRepository.findAll(specification,pageable);
        List<ItemComments> content = itemComments.getContent();
        for(ItemComments comment : content) comment.setRepliesCount(itemCommentRepository.totalReplies(comment.getId()));
        return content;
    }


    public Map<String,Object> addOrUpdateComment(ItemCommentsDto itemCommentsDto , User loggedUser) {
        Map<String,Object> responseObj = new HashMap<>();
        if(!Utils.isEmpty(itemCommentsDto.getCommentSlug())){
            ItemComments itemComments = new ItemComments(loggedUser);
            itemComments.setItemId(itemComments.getItemId());
            itemComments.setMessage(itemComments.getMessage());
            itemComments.setParentId(itemComments.getParentId());
            itemComments = itemCommentRepository.save(itemComments);
            if(itemComments.getId() > 0){
                responseObj.put("message","Comment successfully saved.");
                responseObj.put("status",200);
                return responseObj;
            }
        }else{
            int isUpdated = itemCommentHbRepository.updateComment(itemCommentsDto);
            if(isUpdated > 0){
                responseObj.put("message","Comment successfully updated.");
                responseObj.put("status",201);
                return responseObj;
            }
        }
        responseObj.put("message","Facing some problem to save your comment.");
        responseObj.put("status",400);
        return  responseObj;
    }


}
