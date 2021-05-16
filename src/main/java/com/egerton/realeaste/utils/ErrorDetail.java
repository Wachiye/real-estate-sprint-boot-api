package com.egerton.realeaste.utils;

import lombok.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ErrorDetail {
    private String title;
    private int status;
    private String message;
    private long timestamp;
    private String developer_message;

    Map<String, List<ValidationError>> errors = new HashMap<>();
}