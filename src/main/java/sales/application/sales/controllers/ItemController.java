package sales.application.sales.controllers;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
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

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @PostMapping("/all")
    public ResponseEntity<Page<Item>> getAllItems(@RequestBody ItemDto searchFilters) {
        logger.info("Received request to get all items with filters: {}", searchFilters);
        Page<Item> items = itemService.getAllItem(searchFilters);
        return new ResponseEntity<>(items, HttpStatus.valueOf(200));
    }

    @GetMapping("/detail/{slug}")
    public ResponseEntity<Item> getItemBySlug(@PathVariable String slug) {
        logger.info("Received request to get item detail for slug: {}", slug);
        Item item = itemService.findItemBySLug(slug);
        return new ResponseEntity<>(item, HttpStatus.valueOf(200));
    }

    @Value("${item.absolute}")
    String filePath;

    @GetMapping("/image/{slug}/{filename}")
    public ResponseEntity<Resource> getFile(@PathVariable(required = true) String filename, @PathVariable("slug") String slug) throws Exception {
        logger.info("Received request to get file: {}/{}", slug, filename);
        Path path = Paths.get(filePath + slug + "/" + filename);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.IMAGE_PNG).body(resource);
    }

    @PostMapping("/categories")
    public ResponseEntity<List<ItemCategory>> getAllCategory(@RequestBody SearchFilters searchFilters) {
        logger.info("Received request to get all categories with filters: {}", searchFilters);
        List<ItemCategory> categories = itemService.getAllItemsCategories(searchFilters);
        return new ResponseEntity<>(categories, HttpStatus.valueOf(200));
    }

    @PostMapping("subcategory")
    public ResponseEntity<List<ItemSubCategory>> getItemsSubCategory(@RequestBody SearchFilters searchFilters) {
        logger.info("Received request to get all subcategories with filters: {}", searchFilters);
        List<ItemSubCategory> storeSubCategories = itemService.getAllItemsSubCategories(searchFilters);
        return new ResponseEntity<>(storeSubCategories, HttpStatus.OK);
    }
}
