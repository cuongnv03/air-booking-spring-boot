// File will be removed in the future
// This file is kept as a reference for the search feature of the application
package com.example.airbookingapp.air_booking_app.services.search;

public class SearchCriteria {
    // Getters và Setters
    private String key; // Tên trường
    private String operator; // Toán tử
    private Object value; // Giá trị

    public SearchCriteria(String key, String operator, Object value) {
        this.key = key;
        this.operator = operator;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}

