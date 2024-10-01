package sales.application.sales.repostories;



import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import org.hibernate.transform.AliasToBeanResultTransformer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import sales.application.sales.dto.StoreDto;

import java.util.ArrayList;
import java.util.List;

@Repository
public class StoreHbRepository {

    @Autowired
    EntityManager entityManager;
    /**
     * {
     *     "content": [
     *         {
     *             "id": 17,
     *             "slug": "92d8d129-f9b8-4ca1-b0ce-31554ddb1ef0",
     *             "name": "test kirana store",
     *             "avtar": null,
     *             "address": 33,
     *             "email": "testkirana@gmail.com",
     *             "phone": "9876543219",
     *             "description": "test shop created",
     *             "rating": 0.0,
     *             "status": "A",
     *             "isDeleted": "N",
     *             "createdAt": 1726506666296,
     *             "updatedAt": 1726506666296,
     *             "createdBy": 1,
     *             "updatedBy": 1
     *         },
     *         {
     *             "id": 1,
     *             "slug": "test",
     *             "name": "Swami Kirana Store",
     *             "avtar": null,
     *             "address": 0,
     *             "email": "naresh@gmail.com",
     *             "phone": "9145808226",
     *             "description": "dummy data",
     *             "rating": 4.0,
     *             "status": "A",
     *             "isDeleted": "N",
     *             "createdAt": 1723998053886,
     *             "updatedAt": 1723998053886,
     *             "createdBy": null,
     *             "updatedBy": null
     *         }
     *     ],
     *     "pageable": {
     *         "pageNumber": 0,
     *         "pageSize": 10,
     *         "sort": {
     *             "empty": false,
     *             "unsorted": false,
     *             "sorted": true
     *         },
     *         "offset": 0,
     *         "unpaged": false,
     *         "paged": true
     *     },
     *     "last": true,
     *     "totalPages": 1,
     *     "totalElements": 2,
     *     "first": true,
     *     "size": 10,
     *     "number": 0,
     *     "sort": {
     *         "empty": false,
     *         "unsorted": false,
     *         "sorted": true
     *     },
     *     "numberOfElements": 2,
     *     "empty": false
     * }
     * */





    public List<StoreDto> getAllStores(){
        String hql = "select s.name as storeName from Store s where s.id =:id";
        Query query = entityManager.createQuery(hql);
        query.setParameter("id",1);
        StoreDto storeDto = (StoreDto) query.getSingleResult();
        List resultList = new ArrayList();
        resultList.add(storeDto);

        return resultList;
    }
}
