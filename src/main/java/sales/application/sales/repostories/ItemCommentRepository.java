package sales.application.sales.repostories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.ItemComment;

@Repository
public interface ItemCommentRepository extends JpaRepository<ItemComment,Long>, JpaSpecificationExecutor<ItemComment> {

    @Query("select count(id) from ItemComment where parentId=:parentId")
    Integer totalReplies(@Param("parentId") Long parentId);

    ItemComment findItemCommentBySlug(String slug);
}
