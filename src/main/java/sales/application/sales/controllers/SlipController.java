package sales.application.sales.controllers;

import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.ItemOrderDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.SlipDto;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.SlipItems;
import sales.application.sales.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("slips")
public class SlipController extends CommonService {

    private static final Logger logger = LoggerFactory.getLogger(SlipController.class);

    @PostMapping("all")
    public ResponseEntity<Page<Slip>> getAllSlips(HttpServletRequest request, @RequestBody SearchFilters searchFilters) {
        logger.info("Received request to get all slips with filters: {}", searchFilters);
        User loggedUser = (User) request.getAttribute("user");
        Page<Slip> slips = slipService.getAllActiveSlips(searchFilters, loggedUser);
        return new ResponseEntity<>(slips, HttpStatus.OK);
    }

    @PostMapping("detail/{slipId}")
    public ResponseEntity<Page<SlipItems>> findDistinctSlipItems(HttpServletRequest request, @PathVariable Integer slipId, @RequestBody SearchFilters searchFilters) {
        logger.info("Received request to get slip items for slip ID: {} with filters: {}", slipId, searchFilters);
        Page<SlipItems> slips = itemOrderService.getAllItemOrder(searchFilters, slipId);
        return new ResponseEntity<>(slips, HttpStatus.OK);
    }

    @PostMapping("delete")
    public ResponseEntity<Map<String,Object>> deleteSlip(HttpServletRequest request, @RequestBody Map<String,Object> params) {
        logger.info("Received request to delete slip with params: {}", params);
        User loggedUser = (User) request.getAttribute("user");
        Map<String,Object> responseObj = new HashMap<>();
        int deleteSlip = slipService.deleteSlip(params, loggedUser);
        if(deleteSlip > 0){
            responseObj.put("message","Slip deleted successfully");
            responseObj.put("status",200);
            logger.info("Slip deleted successfully.");
        }else{
            responseObj.put("message","No slip found to delete");
            responseObj.put("status",404);
            logger.info("No slip found to delete.");
        }
        return new ResponseEntity<>(responseObj, HttpStatus.valueOf((Integer) responseObj.get("status")));
    }

    @PostMapping(value = {"add","update"})
    public ResponseEntity<Map<String,Object>> addOrUpdateNewSlip(HttpServletRequest request, @RequestBody SlipDto slipDto) {
        logger.info("Received request to add/update slip: {}", slipDto);
        Map<String,Object> result = new HashMap<>();
        User loggedUser = (User) request.getAttribute("user");
        result = slipService.createOrUpdate(slipDto, loggedUser);
        logger.info("Slip {} successfully.", slipDto.getId() == 0 ? "added" : "updated");
        return new ResponseEntity<>(result, HttpStatus.valueOf((Integer) result.get("status")));
    }

    @PostMapping("add_item")
    public ResponseEntity<Map<String,Object>> addNewOrderInSlip(HttpServletRequest request, @RequestBody ItemOrderDto orderDto) {
        logger.info("Received request to add new order in slip: {}", orderDto);
        Map<String,Object> result = new HashMap<>();
        User loggedUser = (User) request.getAttribute("user");
        result = itemOrderService.saveNewOrder(orderDto, loggedUser);
        logger.info("Order added to slip successfully.");
        return new ResponseEntity<>(result, HttpStatus.valueOf((Integer) result.get("status")));
    }

    @PostMapping("remove-order")
    public ResponseEntity<Map<String,Object>> removeOrderInSlip(@RequestBody Map<String,Object> params) {
        logger.info("Received request to remove order from slip with params: {}", params);
        Map<String,Object> result = new HashMap<>();
        Integer id = (Integer) params.get("id");
        int itemDeleted = slipService.removeSlipItems(id);
        if(itemDeleted > 0){
            result.put("message","Item successfully deleted.");
            result.put("status", 200);
            logger.info("Item successfully deleted from slip.");
        }else {
            result.put("message","No record found to update.");
            result.put("status", 404);
            logger.info("No record found to delete from slip.");
        }

        return new ResponseEntity<>(result, HttpStatus.valueOf((Integer) result.get("status")));
    }


    @Value("${slips.get}")
    String slipsRelativePath;

    @GetMapping("pdf/{slipId}")
    public ResponseEntity<Resource> getSlipPdf(@PathVariable Integer slipId) throws DocumentException, FileNotFoundException, NoSuchObjectException, MalformedURLException {
        Map<String,Object> result = new HashMap<>();
        String slipPdfPath = itemOrderService.createSlipPdf(slipId);
        logger.info("Received request to get pdf file: {}", slipId);
        Path path = Paths.get(slipPdfPath);
        Resource resource = new UrlResource(path.toUri());
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(resource);
    }





}
 