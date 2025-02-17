package sales.application.sales.controllers;


import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sales.application.sales.dto.ItemDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.entities.Item;
import sales.application.sales.entities.ItemCategory;
import sales.application.sales.entities.ItemSubCategory;

@RestController
@RequestMapping("item")
public class ItemController extends CommonService {

    @PostMapping("/all")
    public ResponseEntity< Page<Item>> getAllItems(@RequestBody ItemDto searchFilters) {
        Page<Item> items = itemService.getAllItem(searchFilters);
        return new ResponseEntity<Page<Item>>(items, HttpStatus.valueOf(200));
    }


    @GetMapping("/detail/{slug}")
    public ResponseEntity<Item> getItemBySlug(@PathVariable String slug) {
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


    @PostMapping("/categories")
    public ResponseEntity<List<ItemCategory>> getAllCategory(@RequestBody SearchFilters searchFilters) {
        List<ItemCategory> categories = itemService.getAllItemsCategories(searchFilters);
        return new ResponseEntity<>(categories, HttpStatus.valueOf(200));
    }


    @PostMapping("subcategory")
    public ResponseEntity<List<ItemSubCategory>> getItemsSubCategory(@RequestBody SearchFilters searchFilters) {
        List<ItemSubCategory> storeSubCategories = itemService.getAllItemsSubCategories(searchFilters);
        return new ResponseEntity<>(storeSubCategories, HttpStatus.OK);
    }


}
