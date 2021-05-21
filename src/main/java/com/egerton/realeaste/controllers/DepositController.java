package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.DepositRepo;
import com.egerton.realeaste.dao.TransactionRepo;
import com.egerton.realeaste.models.Deposit;
import com.egerton.realeaste.models.MpesaCallBack;
import com.egerton.realeaste.models.PayRequest;
import com.egerton.realeaste.models.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {
    @Autowired
    private DepositRepo depositRepo;

    @Autowired
    private TransactionRepo transactionRepo;

    @GetMapping
    public List<Deposit> findAllDeposits() {
        return depositRepo.findAll();
    }

    @Autowired
    private Environment env;

    @GetMapping("/{id}")
    public ResponseEntity<Deposit> findDepositById(@PathVariable(value = "id") long id) {
        Optional<Deposit> deposit = depositRepo.findById(id);
        if (deposit.isPresent()) {
            return ResponseEntity.ok().body(deposit.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Deposit saveAgent(@Validated @RequestBody Deposit deposit) {
        return depositRepo.save(deposit);
    }

    @PostMapping("/mpesa/pay")
    public Map<String, Object> makePayment(@Validated @RequestBody PayRequest payRequest) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String timestamp = getTimestamp();

        Map<String, String> map = new HashMap<>();
        map.put("BusinessShortCode", env.getProperty("mpesa_shortcode"));
        map.put("Password", getPassword(timestamp));
        map.put("AccountReference", env.getProperty("mpesa_account_ref"));
        map.put("PartyB", env.getProperty("mpesa_shortcode"));
        map.put("CallBackURL", payRequest.getCallback_url());
        map.put("Amount", payRequest.getAmount());
        map.put("PartyA", payRequest.getPhone());
        map.put("TransactionType", "CustomerPayBillOnline");
        map.put("TransactionDesc", payRequest.getPurpose());
        map.put("PhoneNumber", payRequest.getPhone());
        map.put("PassKey", env.getProperty("mpesa_pass_key"));
        map.put("Timestamp", timestamp);
        HttpEntity<?> request = new HttpEntity<>(map, headers);

        String payment_url = env.getProperty("mpesa_process_url");
        ResponseEntity<String> response = restTemplate.exchange(payment_url, HttpMethod.POST, request, String.class);

        String json = response.getBody();
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> mappedResponse = parser.parseMap(json);

        return mappedResponse;
    }

    @PostMapping("/mpesa/process")
    public Map<String, String> processPayment(@RequestBody String string) throws IOException {
        JsonNode body = new ObjectMapper().readTree(string);
        JsonNode stkCallback = body.get("Body").get("stkCallback");
        JsonNode items = stkCallback.get("CallbackMetadata").get("Item");

        Transaction transaction = new Transaction();

        transaction.setAmount(items.get(0).get("Value").asDouble());
        transaction.setPhoneNumber(items.get(4).get("Value").asLong());
        transaction.setMpesaReceiptNumber(items.get(1).get("Value").textValue());
        transaction.setCheckoutRequestID(stkCallback.get("CheckoutRequestID").textValue());
        transaction.setMerchantRequestID(stkCallback.get("MerchantRequestID").textValue());
        transaction.setResultCode(stkCallback.get("ResultCode").asInt());
        transaction.setResultDesc(stkCallback.get("ResultDesc").textValue());
        transaction.setTransactionDate(items.get(3).get("Value").asLong());
        transaction.setCreated_at(LocalDateTime.now());
        transaction.setAccepted(stkCallback.get("ResultCode").textValue() == "0" ? true : false);
        transaction.setUsed(false);

        transactionRepo.save(transaction);

        Map<String, String> response = new HashMap<>();
        response.put("ResultCode", "0");
        response.put("ResultDesc", "Accepted");

        return response;
    }

    @GetMapping("/check/{checkout_id}")
    public Map<String, Object> checkTransaction(@PathVariable(value = "checkout_id") String checkout_id) {
        Optional<Transaction> transaction = transactionRepo.getTransactionsByCheckoutRequestID(checkout_id);
        Map<String, Object> response = new HashMap<>();

        if (transaction.isPresent()) {
            response.put("mpesa_code", transaction.get().getMpesaReceiptNumber());
        } else {
            response.put("mpesa_code", null);
        }
        return response;
    }
//    @GetMapping("/mpesa/validation")
//    public Map<String,String> mpesaValidate(){
//        Map<String,String> map = new HashMap<>();
//        map.put("ResultCode","0");
//        map.put("ResultDesc", "Accepted");
//        return map;
//    }

//    @PostMapping("/mpesa/confirm")
//    public  Map<String,String> mpesaConfirm(@RequestBody String string){
//        System.out.println(string);
//        Map<String,String> map = new HashMap<>();
//        map.put("ResultCode","0");
//        map.put("ResultDesc", "Accepted");
//        return map;
//    }

    //    @GetMapping("/mpesa/register")
//    public Map<String, Object> registerMpesaURLs(){
//        String register_url = "https://sandbox.safaricom.co.ke/mpesa/c2b/v1/registerurl";
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setBearerAuth(getToken());
//        headers.setContentType(MediaType.APPLICATION_JSON);
//
//        Map<String, String> map  = new HashMap<>();
//        map.put("ShortCode","174379");
//        map.put("ConfirmationURL","https://dfed33f017e9.ngrok.io/api/deposits/mpesa/confirm");
//        map.put("ValidationURL", "https://dfed33f017e9.ngrok.io/api/deposits/mpesa/validate");
//        map.put("ResponseType","Completed");
//
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpEntity<?> request = new HttpEntity<>(map, headers);
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange(register_url, HttpMethod.POST, request, String.class);
//
//        String json = responseEntity.getBody();
//        JsonParser parser = JsonParserFactory.getJsonParser();
//        Map<String, Object> mapped = parser.parseMap(json);
//
//        System.out.println(mapped);
//        return mapped;
//    }
//
    public String getToken() {
        String token = null;

        RestTemplate restTemplate = new RestTemplate();

        String username = env.getProperty("mpesa_consumer_key");
        String password = env.getProperty("mpesa_consumer_secret");
        String token_params = username + ":" + password;
        String token_url = env.getProperty("mpesa_token_url");

        HttpHeaders headers = new HttpHeaders();

        headers.set("x-requested-with", "mobile");
        headers.set("Authorization", "Basic " + getBase64(token_params));
        headers.set("Content-Type", "application/json");

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(token_url, HttpMethod.GET, request, String.class);

        String json = responseEntity.getBody();
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = parser.parseMap(json);
        token = (String) map.get("access_token");

        return token;
    }

    public String getPassword(String timestamp) {
        String string = env.getProperty("mpesa_shortcode") + env.getProperty("mpesa_pass_key") + timestamp;
        return getBase64(string);
    }

    public String getTimestamp() {
        Date date = new Date();
        Long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        String t = ts.toString().replace("-", "");
        t = t.replace(" ", "");
        t = t.replace(":", "");
        t = t.substring(0, 14);
        return t;
    }

    public String getBase64(String string) {
        return Base64.getEncoder().encodeToString(string.getBytes());
    }

}
