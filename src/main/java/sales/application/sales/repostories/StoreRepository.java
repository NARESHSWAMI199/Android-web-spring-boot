package sales.application.sales.repostories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.Store;

import java.util.Map;

@Repository
public interface StoreRepository extends JpaRepository<Store,Long>, JpaSpecificationExecutor<Store> {

    @Query(value = "select s.name as StoreName, " +
        "s.avtar as storeAvtar," +
        "s.slug as storeSlug , " +
        "s.created_at as createdAt," +
        "s.created_by as createBy," +
        "s.discription as description," +

        "s.email as storeEmail , " +
        "s.phone as storeContact , " +
        "u.slug as  userSlug , " +
        "u.username as username , " +
        "u.avtar as userAvtar , " +
        "u.user_type as userType, " +

        "c.city_name as city, " +
        "st.state_name as state " +

        "from store s left join user u on u.user_id = s.user_id  " +
            "left join address a on a.id = s.address " +
            "left join city c on c.id= a.city " +
            "left join state st on st.id = c.state_id  where s.is_deleted != 'Y' and s.name=:searchKey ",nativeQuery = true)
    Page<Map<String, Object>> findAllStore(Pageable pageable, String searchKey);



    Store findStoreBySlug(String slug);


}
