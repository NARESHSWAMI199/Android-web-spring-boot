package sales.application.sales.controllers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.ItemDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.Store;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@RestController
@RequestMapping("item")
public class ItemController extends CommonService {

    /**@PostMapping("/all")
    public ResponseEntity< Page<Map<String,Object>>> getAllStore(@RequestBody SearchFilters searchFilters) {
        Page<Map<String,Object>> stores = itemService.getAllItems(searchFilters);
        return new ResponseEntity<Page<Map<String,Object>>>(stores, HttpStatus.valueOf(200));
    }*/



    @PostMapping("/all")
    public ResponseEntity< Page<Item>> getAllStore(@RequestBody ItemDto searchFilters) {
        Page<Item> items = itemService.getAllItem(searchFilters);
        return new ResponseEntity<Page<Item>>(items, HttpStatus.valueOf(200));
    }


    @GetMapping("/detail/{slug}")
    public ResponseEntity<Item> getAllStore(@PathVariable String slug) {
        Item item = itemService.findItemBySLug(slug);
        return new ResponseEntity<Item>(item, HttpStatus.valueOf(200));
    }

    @Value("${item.absolute}")
    String filePath;

    @GetMapping("/image/{slug}/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable(required = true) String filename, @PathVariable("slug") String slug ) throws Exception {
        Path path = Paths.get(filePath +slug+ "/"+filename);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
    }


}
