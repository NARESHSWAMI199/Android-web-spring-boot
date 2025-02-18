package sales.application.sales.services;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.SlipAndOrderDto;
import sales.application.sales.dto.SlipDto;
import sales.application.sales.entities.Slip;
import sales.application.sales.entities.User;
import sales.application.sales.exceptions.MyException;
import sales.application.sales.utilities.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Service
public class SlipService extends CommonRepository {

    private static final Logger logger = LoggerFactory.getLogger(SlipService.class);

    public Page<Slip> getAllActiveSlips(SearchFilters searchFilters, User loggedUser) {
        logger.info("Received request to get all active slips with filters: {}", searchFilters);
        Pageable pageable = getPageable(searchFilters);
        Page<Slip> slips = slipRepository.findAllByUserId(loggedUser.getId(), pageable);
        logger.info("Returning all active slips for user ID: {}", loggedUser.getId());
        return slips;
    }

    public Map<String, Object> createOrUpdate(SlipDto slipDto, User loggedUser) {
        logger.info("Received request to create/update slip: {}", slipDto);
        Map<String, Object> result = new HashMap<>();
        if (slipDto.getId() < 1) {
            Slip slip = createNewSlip(slipDto, loggedUser);
            if (slip.getId() > 0) {
                result.put("res", slip);
                result.put("message", "Slip successfully created.");
                result.put("status", 201);
                logger.info("Slip created successfully with ID: {}", slip.getId());
            } else {
                result.put("message", "Something went wrong during insert.");
                result.put("status", 400);
                logger.error("Error creating slip: {}", slipDto);
            }
        } else {
            int isUpdated = updateSlip(slipDto, loggedUser);
            if (isUpdated > 0) {
                result.put("message", "Slip successfully updated.");
                result.put("status", 200);
                logger.info("Slip updated successfully with ID: {}", slipDto.getId());
            } else {
                result.put("message", "Something went wrong during update.");
                result.put("status", 200);
                logger.error("Error updating slip: {}", slipDto);
            }
        }
        return result;
    }

    public Slip createNewSlip(SlipDto slipDto, User loggedUser) {
        logger.info("Creating new slip: {}", slipDto);
        if (Utils.isEmpty(slipDto.getName())) throw new MyException("Slip name can't be blank.");
        Slip slip = Slip.builder()
                .slipName(slipDto.getName())
                .userId(loggedUser.getId())
                .createdAt(Utils.getCurrentMillis())
                .updatedAt(Utils.getCurrentMillis())
                .isArchived("N")
                .isDeleted("N")
                .updatedBy(loggedUser.getId())
                .build();
        Slip savedSlip = slipRepository.save(slip);
        logger.info("New slip created with ID: {}", savedSlip.getId());
        return savedSlip;
    }

    @Transactional
    public int updateSlip(SlipDto slipDto, User loggedUser) {
        logger.info("Updating slip: {}", slipDto);
        int isUpdated = slipHbRepository.updateSlip(slipDto, loggedUser);
        logger.info("Slip updated with ID: {}", slipDto.getId());
        return isUpdated;
    }

    public Slip findSlipById(Integer id) {
        logger.info("Finding slip by ID: {}", id);
        Slip slip = slipRepository.findById(id).orElse(null);
        if (slip == null) {
            logger.error("Slip ID {} doesn't exist", id);
            throw new MyException("Slip id doesn't exist");
        }
        logger.info("Slip found with ID: {}", id);
        return slip;
    }

    @Transactional
    public int addNewItemInSlip(SlipAndOrderDto slipAndOrder) {
        logger.info("Adding new item in slip: {}", slipAndOrder);
        int slipId = slipAndOrder.getSlipId();
        int orderId = slipAndOrder.getOrderID();
        if (slipId != 0 && orderId != 0) {
            int isUpdated = slipHbRepository.insertNewOrderInSlip(slipId, orderId);
            logger.info("New item added in slip with ID: {}", slipId);
            return isUpdated;
        }
        logger.error("Invalid slip ID or order ID: {}", slipAndOrder);
        return 0;
    }

    public int removeSlipItems(Integer id) {
        logger.info("Removing slip items with ID: {}", id);
        int deleted = slipItemsHbRepository.deleteSlipItems(id);
        logger.info("Slip items removed with ID: {}", id);
        return deleted;
    }

    public int deleteSlip(Map<String, Object> params, User loggedUser) {
        logger.info("Deleting slip with params: {}", params);
        Integer slipId = (Integer) params.get("id");
        int deleted = slipHbRepository.deleteSlip(slipId, loggedUser.getId());
        logger.info("Slip deleted with ID: {}", slipId);
        return deleted;
    }
}
