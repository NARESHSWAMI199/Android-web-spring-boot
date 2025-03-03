package sales.application.sales.services;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import jakarta.transaction.Transactional;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import sales.application.sales.dto.ItemOrderDto;
import sales.application.sales.dto.SearchFilters;
import sales.application.sales.dto.SlipAndOrderDto;
import sales.application.sales.entities.*;
import sales.application.sales.exceptions.MyException;
import sales.application.sales.utilities.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.rmi.NoSuchObjectException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class ItemOrderService  extends CommonRepository {

    private static final Logger logger = LoggerFactory.getLogger(ItemOrderService.class);

    @Autowired
    SlipService slipService;


    public Page<SlipItems> getAllItemOrder(SearchFilters searchFilters,int slipId){
        logger.info("Received request to get all item orders for slip ID: {} with filters: {}", slipId, searchFilters);
        Pageable pageable = getPageable(searchFilters);
        return slipItemsRepository.findDistinctBySlipId(slipId, pageable);
    }

    @Transactional
    public Map<String,Object> saveNewOrder(ItemOrderDto itemOrderDto, User loggedUser){
        logger.info("Received request to save new order: {}", itemOrderDto);
        Map<String,Object> result = new HashMap<>();
        try {
            ItemOrder itemOrder = new ItemOrder();
            Optional<Item> item = itemRepository.findById(Long.valueOf(itemOrderDto.getItemId()));
            item.orElseThrow();
            itemOrder.setItem(item.get());
            itemOrder.setUserId(loggedUser.getId());
            Integer quantity = itemOrderDto.getQuantity();
            if (quantity == null || quantity == 0) {
                quantity = 1;
            }
            itemOrder.setQuantity(quantity);
            itemOrder.setCreatedAt(Utils.getCurrentMillis());
            itemOrder.setUpdatedAt(Utils.getCurrentMillis());
            itemOrder.setStatus("A");
            itemOrder.setIsDeleted("N");
            itemOrder.setUpdatedBy(loggedUser.getId());
            itemOrder = itemOrderRepository.save(itemOrder);
            logger.info("Order with ID {} saved successfully.", itemOrder.getId());



            Slip slip = slipService.findSlipById(itemOrderDto.getSlipId());
            /* Adding this order directly in slip */
            boolean orderedAddedInSlip = saveItemOrderInSlip(itemOrderDto.getSlipId(), itemOrder.getId());
            if(orderedAddedInSlip){
                result.put("message","Item successfully added in "+slip.getSlipName());
                result.put("status", 200);
                logger.info("Item successfully added in slip: {}", slip.getSlipName());
            }else {
                throw new MyException("We facing some issue during add item in "+slip);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();;
            logger.error("Error saving new order: {}", itemOrderDto, e);
            throw  e;
        }
        return result;
    }


    public boolean saveItemOrderInSlip(int slipId,int itemOrderId){
        logger.info("Saving item order ID {} in slip ID {}", itemOrderId, slipId);
        SlipAndOrderDto slipAndOrder = new SlipAndOrderDto();
        slipAndOrder.setOrderID(itemOrderId);
        slipAndOrder.setSlipId(slipId);
       int isUpdated = slipService.addNewItemInSlip(slipAndOrder);
       boolean result = isUpdated > 0;
       logger.info("Item order ID {} saved in slip ID {}: {}", itemOrderId, slipId, result);
       return result;
    }



    public void createSlipPdf(Integer slipId) throws FileNotFoundException, DocumentException, NoSuchObjectException {
        Document document = new Document();
        Slip slip = slipService.findSlipById(slipId);
        if(slip == null) throw new NoSuchObjectException("No slip found.");
        String slipName = Utils.toTitleCase(slip.getSlipName());
        PdfWriter.getInstance(document, new FileOutputStream("iTextTable.pdf"));
        List<SlipItems> slipItems = slipItemsRepository.findBySlipId(slipId);
        document.open();

        // Heading
        Font headingFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Paragraph heading = new Paragraph(slipName,headingFont);
        heading.setAlignment(Element.ALIGN_CENTER);
        heading.setSpacingAfter(20);
        document.add(heading);


        PdfPTable table = new PdfPTable(6);
        addTableHeader(table);
        addRows(table,slipItems);
        document.add(table);
        document.close();
    }


    private void addTableHeader(PdfPTable table) {
        Stream.of("Item", "Quantity","Price","Discount","Total")
                .forEach(columnTitle -> {
                    PdfPCell header = new PdfPCell();
                    if(columnTitle.equals("Item")){
                        header.setColspan(2);
                    }
                    header.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    header.setBorderWidth(1);
                    header.setPhrase(new Phrase(columnTitle));
                    table.addCell(header);
                });
    }


    private void addRows(PdfPTable table, List<SlipItems> slipItemsList) {
        Float totalPrice = 0f;
        for(SlipItems slipItems : slipItemsList) {
            ItemOrder itemOrder = slipItems.getItemOrder();
            if (itemOrder != null) {
                Item item = itemOrder.getItem();
                int quantity = itemOrder.getQuantity();
                String itemPrice = (item.getPrice() - item.getDiscount()) * quantity + "";
                // Item Name take three columns
                PdfPCell itemNameCell = new PdfPCell(new Paragraph(item.getName()));
                itemNameCell.setColspan(2);
                table.addCell(itemNameCell);
                // -- end Item name block
                table.addCell(item.getPrice() + " x "+ quantity);
                table.addCell((item.getPrice() * quantity) + "");
                table.addCell((item.getDiscount() * quantity) + "");
                table.addCell(itemPrice);
                totalPrice += Float.valueOf(itemPrice);
            }
        }

            // Add cells with varying spans (flex-like behavior)
            PdfPCell cell1 = new PdfPCell(new Paragraph("Total"));
            cell1.setColspan(3);
            cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(new Paragraph(totalPrice+""));
            cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell2.setColspan(3);

            table.addCell(cell2);


    }


}
