package com.example.airbookingapp.air_booking_app.dto.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchFlightRequest {
    private String key;
    private String value;
    private QueryOperator operator;
    private List<String> values;
}
