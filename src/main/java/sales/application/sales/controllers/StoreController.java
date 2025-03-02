package sales.application.sales.controllers;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.RatingDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.StoreDto;
import sales.application.sales.entities.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("store")
public class StoreController extends CommonService {

    private static final Logger logger = LoggerFactory.getLogger(StoreController.class);

    @PostMapping("/all")
    public ResponseEntity<Page<Store>> getAllStore(@RequestBody SearchFilters searchFilters) {
        logger.info("Received request to get all stores with filters: {}", searchFilters);
        Page<Store> stores = storeService.getAllStore(searchFilters);
        return new ResponseEntity<>(stores, HttpStatus.valueOf(200));
    }


    @GetMapping("/detail/{slug}")
    public ResponseEntity<Store> getStoreDetailBySlug(@PathVariable String slug) {
        logger.info("Received request to get store detail for slug: {}", slug);
        Store stores = storeService.findStoreBySlug(slug);
        return new ResponseEntity<Store>(stores, HttpStatus.valueOf(200));
    }


    @GetMapping("/-detail/{id}")
    public ResponseEntity<Map<String,String>> findStoreId(@PathVariable Integer id) {
        logger.info("Received request to get store name for ID: {}", id);
        Map<String,String> result = new HashMap<>();
        String storeName = storeService.getStoreNameById(id);
        result.put("storeName" , storeName);
        return new ResponseEntity<>(result, HttpStatus.valueOf(200));
    }


    @Value("${store.absolute}")
    String filePath;

    @GetMapping("/image/{slug}/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable(required = true) String filename, @PathVariable("slug") String slug ) throws Exception {
        logger.info("Received request to get file: {}/{}", slug, filename);
        Path path = Paths.get(filePath +slug+ "/"+filename);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
    }


    @PostMapping("/categories")
    public ResponseEntity<List<StoreCategory>> getAllCategory(@RequestBody SearchFilters searchFilters) {
        logger.info("Received request to get all categories with filters: {}", searchFilters);
        List<StoreCategory> categories = storeService.getAllStoreCategories(searchFilters);
        return new ResponseEntity<>(categories, HttpStatus.valueOf(200));
    }


    @PostMapping("subcategory")
    public ResponseEntity<List<StoreSubCategory>> getStoreSubCategory(@RequestBody SearchFilters searchFilters) {
        logger.info("Received request to get all subcategories with filters: {}", searchFilters);
        List<StoreSubCategory> storeSubCategories = storeService.getAllStoreSubCategories(searchFilters);
        return new ResponseEntity<>(storeSubCategories, HttpStatus.OK);
    }

    @GetMapping("subcategory/{subcategoryId}")
    public ResponseEntity<StoreSubCategory> getStoreSubCategory(@PathVariable(required = true) Integer subcategoryId) {
        logger.info("Received request to get subcategory detail for ID: {}", subcategoryId);
        StoreSubCategory storeSubCategory = storeService.getStoreSubcategoryDetail(subcategoryId);
        return new ResponseEntity<>(storeSubCategory, HttpStatus.OK);
    }


    @GetMapping("ratings/{storeSlug}")
    public ResponseEntity<Map<String,Object>> getRatingCountByItemSlug(@PathVariable("storeSlug") String storeSlug) {
        Map<String,Object> result = new HashMap<>();
        Long ratingCount = storeService.getRatingCountByStoreSlug(storeSlug);
        result.put("totalRating",ratingCount);
        return new ResponseEntity<>(result,HttpStatus.valueOf(200));
    }

    @PostMapping("update/ratings")
    @Transactional
    public ResponseEntity<Map<String,Object>> handleItemRatings(@RequestBody RatingDto ratingDto, HttpServletRequest request){
        logger.info("Going to update or store ratings : {} ",ratingDto);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> result = new HashMap<>();
        Float ratingAvg = itemService.updateRatings(ratingDto, loggedUser);
        if(ratingAvg > 0){
            result.put("ratingAvg",ratingAvg);
            result.put("message","Your ratings successfully updated.");
            result.put("status",201);
            logger.info("Store ratings successfully updated.");
        }else{
            result.put("message","No record found to update.");
            result.put("status",404);
            logger.info("No record found to update");
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            }catch (Exception e){
                logger.error("facing err during rollback store ratings : {} ",e.getMessage());
            }
        }
        return new ResponseEntity<>(result,HttpStatus.valueOf((Integer) result.get("status")));
    }


}
