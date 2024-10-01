package sales.application.sales.controllers;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.StoreDto;
import sales.application.sales.entities.Store;

import java.util.Map;

@RestController
@RequestMapping("store")
public class StoreController extends CommonService {

    @PostMapping("/all")
    public ResponseEntity< Page<Map<String,Object>>> getAllStore(@RequestBody SearchFilters searchFilters) {
        Page<Map<String,Object>> stores = storeService.getAllStores(searchFilters);
        return new ResponseEntity<Page<Map<String,Object>>>(stores, HttpStatus.valueOf(200));
    }


    @GetMapping("/detail/{slug}")
    public ResponseEntity<Store> getAllStore(@PathVariable String slug) {
        Store stores = storeService.findStoreBySlug(slug);
        return new ResponseEntity<Store>(stores, HttpStatus.valueOf(200));
    }



}
