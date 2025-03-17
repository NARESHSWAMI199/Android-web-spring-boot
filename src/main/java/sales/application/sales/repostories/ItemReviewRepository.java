package sales.application.sales.repostories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sales.application.sales.dto.ReviewListDto;
import sales.application.sales.entities.ItemReview;

@Repository
public interface ItemReviewRepository extends JpaRepository<ItemReview,Long>, JpaSpecificationExecutor<ItemReview> {

    @Query("select count(id) from ItemReview where parentId=:parentId")
    Integer totalReplies(@Param("parentId") Long parentId);


    @Query(value = """
            SELECT 
                new sales.application.sales.dto.ReviewListDto(
                    u.username,
                    ir
                )
           FROM ItemReview ir 
           JOIN User u ON u.id=ir.userId
           WHERE ir.itemId=:itemId
        """)
    Page<ReviewListDto> getAllReviewByItemId(Long itemId, Pageable pageable);

    ItemReview findItemReviewBySlug(String slug);
}
