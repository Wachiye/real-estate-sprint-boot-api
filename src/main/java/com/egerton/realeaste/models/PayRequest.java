package com.egerton.realeaste.models;

import lombok.Data;

@Data
public class PayRequest {
    private String phone;
    private String amount;
    private String purpose;
    private String callback_url;
}
