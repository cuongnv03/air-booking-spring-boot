package com.example.airbookingapp.air_booking_app.data.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtLoginSuccessResponse {
    private boolean success;
    private String token;
    private String username;
    private String expiryDate;

    @Override
    public String toString() {
        return "JWTLoginSuccessResponse{" +
                "success=" + success +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", expireDate='" + expiryDate + '\'' +
                '}';
    }
}