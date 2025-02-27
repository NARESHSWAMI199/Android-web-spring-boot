package sales.application.sales.repostories;


import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.StoreSubCategory;

import java.util.List;

@Repository
public interface StoreSubCategoryRepository extends JpaRepository<StoreSubCategory,Integer> {
    @Query(value = "from StoreSubCategory ssc where ssc.categoryId =:categoryId")
    List<StoreSubCategory> getSubCategories(@Param("categoryId") int categoryId, Pageable pageable);

    @Query(value = "select id from StoreSubCategory ssc where ssc.slug =:slug")
    Integer getStoreSubCategoryIdBySlug(@Param("slug") String slug);

}
