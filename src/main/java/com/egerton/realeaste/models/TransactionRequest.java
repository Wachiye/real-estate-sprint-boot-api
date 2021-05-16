package com.egerton.realeaste.models;

import lombok.Data;

import java.util.Map;

@Data
public class TransactionRequest {
    private String MerchantRequestID;
    private String CheckoutRequestID;
    private int ResultCode;
    private String ResultDesc;
    private CallBackMetadata callBackMetadata;

    public class CallBackMetadata{
        private Map<String, String> Item;
    }
}
