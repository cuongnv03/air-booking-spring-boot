package com.example.airbookingapp.air_booking_app.data.request;

import lombok.Data;

import java.util.List;

@Data
public class SearchFlightRequest {
    private String key;
    private String operator;
    private String value;
    private List<String> values;
}
