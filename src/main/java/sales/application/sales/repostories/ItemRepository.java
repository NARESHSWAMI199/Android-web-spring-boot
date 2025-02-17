package sales.application.sales.repostories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sales.application.sales.entities.Item;

import java.util.Map;


@Repository
public interface ItemRepository extends JpaRepository<Item,Long> , JpaSpecificationExecutor<Item> {


@Query(value = """
    select i.slug as itemSlug,
           i.name as itemName,
           i.label as label,
           i.price as price,
           i.discount as discount,
           i.description as description,
           i.avatar as itemAvatar,
           i.rating as rating,
           s.slug as storeSlug,
           s.name as storeName,
           a.zip_code as zipCode
    from item i 
    inner join store s on s.id = i.wholesale_id and s.is_deleted != 'Y' 
    left join address a on a.id = s.address
    where i.is_deleted != 'Y' and i.status != 'D'
    order by abs(cast(a.zip_code as int) - cast(:userZipCode as int)), i.rating desc
    """, nativeQuery = true)
public Page<Map<String, Object>> findAllItem(Pageable pageable, String userZipCode);


    Item findItemBySlug(String slug);
}
