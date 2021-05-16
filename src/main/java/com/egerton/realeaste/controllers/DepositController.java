package com.egerton.realeaste.controllers;

import com.egerton.realeaste.dao.DepositRepo;
import com.egerton.realeaste.models.Deposit;
import com.egerton.realeaste.models.PayRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JsonParser;
import org.springframework.boot.json.JsonParserFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/api/deposits")
public class DepositController {
    @Autowired
    private DepositRepo depositRepo;

    @GetMapping
    public List<Deposit> findAllDeposits(){
        return depositRepo.findAll();
    }

    @Autowired
    private Environment env;

    @GetMapping("/{id}")
    public ResponseEntity<Deposit> findDepositById(@PathVariable(value = "id") long id) {
        Optional<Deposit> deposit = depositRepo.findById(id);
        if(deposit.isPresent()){
            return ResponseEntity.ok().body(deposit.get());
        }
        else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public Deposit saveAgent(@Validated @RequestBody Deposit deposit) {
        return depositRepo.save(deposit);
    }

    @PostMapping("/mpesa/pay")
    public String makePayment(@Validated @RequestBody PayRequest payRequest){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String timetamp = getTimestamp();

        Map<String, String> map  = new HashMap<>();
        map.put("BusinessShortCode", env.getProperty("mpesa_shortcode"));
        map.put("Password", getPassword(timetamp));
        map.put("AccountReference", env.getProperty("mpesa_account_ref"));
        map.put("PartyB", env.getProperty("mpesa_shortcode"));
        map.put("CallBackURL", payRequest.getCallback_url());
        map.put("Amount", payRequest.getAmount());
        map.put("PartyA", payRequest.getPhone());
        map.put("TransactionType", "CustomerBuyGoodsOnline");
        map.put("TransactionDesc",payRequest.getPurpose());
        map.put("PhoneNumber", payRequest.getPhone());
        map.put("PassKey",env.getProperty("mpesa_pass_key"));
        map.put("Timestamp", timetamp);
        HttpEntity<?> request = new HttpEntity<>(map,headers);

        String payment_url = env.getProperty("mpesa_process_url");
        ResponseEntity<String> response = restTemplate.exchange(payment_url,HttpMethod.POST,request,String.class);

        String json = response.getBody();
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> mapped = parser.parseMap(json);

        System.out.println(mapped);

        return mapped.get("CustomerMessage").toString();
    }

    @PostMapping("/mpesa/process")
    public String processPayment(@Validated @RequestBody Object object){
        System.out.println(object);
        return "XNDHR658";
    }
    public String getToken(){
        String token = null;

        RestTemplate restTemplate = new RestTemplate();

        String username = env.getProperty("mpesa_consumer_key");
        String password = env.getProperty("mpesa_consumer_secret");
        String token_params = username + ":" + password;
        String token_url = env.getProperty("mpesa_token_url");

        HttpHeaders headers = new HttpHeaders();

        headers.set("x-requested-with", "mobile");
        headers.set("Authorization", "Basic " + getBase64(token_params));
        headers.set("Content-Type","application/json");

        HttpEntity<?> request = new HttpEntity<>(headers);
        ResponseEntity<String> responseEntity = restTemplate.exchange(token_url, HttpMethod.GET, request, String.class);

        String json = responseEntity.getBody();
        JsonParser parser = JsonParserFactory.getJsonParser();
        Map<String, Object> map = parser.parseMap(json);
        token = (String) map.get("access_token");

        return token;
    }

    public String getPassword(String timestamp){
        String string = env.getProperty("mpesa_shortcode") + env.getProperty("mpesa_pass_key") + timestamp;
        return  getBase64(string);
    }

    public String getTimestamp(){
        Date date = new Date();
        Long time = date.getTime();
        Timestamp ts = new Timestamp(time);
        String t = ts.toString().replace("-","");
        t = t.replace(" ","");
        t = t.replace(":","");
        t = t.substring(0, 14);
        return t;
    }
    public String getBase64(String string){
        return Base64.getEncoder().encodeToString( string.getBytes());
    }

}
