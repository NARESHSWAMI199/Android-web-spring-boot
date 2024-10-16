package sales.application.sales.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.ItemCommentsFilterDto;
import sales.application.sales.entities.ItemComments;

import java.util.List;

@Service
public class ItemCommentService {

//    public List<ItemComments> getALlItemComment(ItemCommentsFilterDto filters) {
//        Specification<ItemComments> specification = Specification.where(
//                (containsName(filters.getSearchKey()))
//                        .and(greaterThanOrEqualFromDate(filters.getFromDate()))
//                        .and(lessThanOrEqualToToDate(filters.getToDate()))
//                        .and(hasSlug(filters.getSlug()))
//                        .and(isItemId(filters.getItemId()))
//                        .and(isParentComment(filters.getParentId()))
//        );
//        Pageable pageable = getPageable(filters);
//        Page<ItemComments> itemComments = itemCommentRepository.findAll(specification,pageable);
//        List<ItemComments> content = itemComments.getContent();
//        for(ItemComments comment : content) comment.setRepliesCount(itemCommentRepository.totalReplies(comment.getId()));
//        return content;
//    }
}
