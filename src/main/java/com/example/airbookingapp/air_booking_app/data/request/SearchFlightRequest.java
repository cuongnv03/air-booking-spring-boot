package com.example.airbookingapp.air_booking_app.data.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFlightRequest {
    private String key;
    private String operator;
    private String value;
    private List<String> values;
}
