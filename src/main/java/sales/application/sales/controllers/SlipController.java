package sales.application.sales.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sales.application.sales.dto.ItemOrderDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.SlipDto;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.SlipItems;
import sales.application.sales.entities.User;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("slips")
public class SlipController extends CommonService {



    @PostMapping("all")
    public ResponseEntity<Page<Slip>> getAllSlips(HttpServletRequest request, @RequestBody SearchFilters searchFilters) {
        User loggedUser = (User) request.getAttribute("user");
        Page<Slip> slips = slipService.getAllActiveSlips(searchFilters, loggedUser);
        return new ResponseEntity<>(slips, HttpStatus.OK);
    }


    @PostMapping("detail/{slipId}")
    public ResponseEntity<Page<SlipItems>> findDistinctSlipItems (HttpServletRequest request, @PathVariable Integer slipId, @RequestBody SearchFilters searchFilters) {
        Page<SlipItems> slips = itemOrderService.getAllItemOrder(searchFilters,slipId);
        return new ResponseEntity<>(slips, HttpStatus.OK);
    }

    @PostMapping(value = {"add","update"})
    public ResponseEntity<Map<String,Object>> addOrUpdateNewSlip (HttpServletRequest request,@RequestBody SlipDto slipDto ) {
        Map<String,Object> result = new HashMap<>();
        User loggedUser = (User) request.getAttribute("user");
        result = slipService.createOrUpdate(slipDto,loggedUser);
        return new ResponseEntity<>(result,HttpStatus.valueOf((Integer) result.get("status")));
    }

    @PostMapping("add_item")
    public ResponseEntity<Map<String,Object>> addNewOrderInSlip (HttpServletRequest request,@RequestBody ItemOrderDto orderDto ) {
        Map<String,Object> result = new HashMap<>();
        User loggedUser = (User) request.getAttribute("user");
        result = itemOrderService.saveNewOrder(orderDto,loggedUser);
        return new ResponseEntity<>(result,HttpStatus.valueOf((Integer) result.get("status")));
    }


}
