package sales.application.sales.controllers;


import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.Store;

import java.util.Map;

@RestController
@RequestMapping("item")
public class ItemController extends CommonService {

    @PostMapping("/all")
    public ResponseEntity< Page<Map<String,Object>>> getAllStore(@RequestBody SearchFilters searchFilters) {
        Page<Map<String,Object>> stores = itemService.getAllItem(searchFilters);
        return new ResponseEntity<Page<Map<String,Object>>>(stores, HttpStatus.valueOf(200));
    }


    @GetMapping("/detail/{slug}")
    public ResponseEntity<Item> getAllStore(@PathVariable String slug) {
        Item item = itemService.findItemBySLug(slug);
        return new ResponseEntity<Item>(item, HttpStatus.valueOf(200));
    }



}
