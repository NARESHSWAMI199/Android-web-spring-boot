package sales.application.sales.controllers;

import com.paytm.pg.merchant.PaytmChecksum;
import jakarta.servlet.http.HttpServletRequest;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import sales.application.sales.utilities.OrderedJSONObject;

import java.net.URL;
import java.util.Map;
import java.util.Random;

@RestController
public class PaytmPg {


    /**-------------------------------
        Test Merchant ID = GqRqbh04018127719079
        Test Merchant Key = xaQoD3kKpMfzlxAg
        Website = WEBSTAGING
        Industry Type = Retail
        Channel ID (For Website) = WEB
        Channel ID (For Mobile Apps) = WAP
     */

    @RequestMapping("/paytm/call")
    public ResponseEntity<Map> paytmGetwaty(){
        ResponseEntity<Map> response=null;
    try {
        String mid = "GqRqbh04018127719079";
//        mid = "RHLBnF07039627707073";
        String key = "xaQoD3kKpMfzlxAg";
//        key = "BXWmXOntYo858BTG";
        OrderedJSONObject paytmParams = new OrderedJSONObject();
        Random random = new Random();
        String orderId = "ORDER_"+random.nextInt(10000000);

        OrderedJSONObject body = new OrderedJSONObject();
        body.put("requestType", "Payment");
        body.put("mid", mid);
        body.put("websiteName", "WEBSTAGING");
        body.put("orderId", orderId);
        body.put("callbackUrl", "http://localhost:8082/pay");

        OrderedJSONObject txnAmount = new OrderedJSONObject();
        txnAmount.put("value", "50.00");
        txnAmount.put("currency", "INR");

        OrderedJSONObject userInfo = new OrderedJSONObject();
        userInfo.put("custId", "CUST_001");


        body.put("txnAmount", txnAmount);
        body.put("userInfo", userInfo);

        /*
         * Generate checksum by parameters we have in body
         * You can get Checksum JAR from https://developer.paytm.com/docs/checksum/
         * Find your Merchant Key in your Paytm Dashboard at https://dashboard.paytm.com/next/apikeys
         */

            String checksum = null;
            try {
                checksum = PaytmChecksum.generateSignature(body.toString(), key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            OrderedJSONObject head = new OrderedJSONObject();
            head.put("signature", checksum);

            paytmParams.put("body", body);
            paytmParams.put("head", head);

            String post_data = paytmParams.toString();

            /* for Staging */
            URL url = new URL("https://securegw-stage.paytm.in/theia/api/v1/initiateTransaction?mid="+mid+"&orderId="+orderId);

            /* for Production */
            // URL url = new URL("https://securegw.paytm.in/theia/api/v1/initiateTransaction?mid=YOUR_MID_HERE&orderId=ORDERID_98765");
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String,Object>> httpEntity = new HttpEntity<>(paytmParams.toMap(),headers);
            System.err.println(post_data);
            RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.postForEntity(url.toString(), httpEntity, Map.class);
    } catch (Exception exception) {
        exception.printStackTrace();
    }
    return response;
}

    @RequestMapping("pay")
    public String callbackUrl(HttpServletRequest request){
        System.out.println(request.getQueryString());
        return request.getQueryString();
    }
}