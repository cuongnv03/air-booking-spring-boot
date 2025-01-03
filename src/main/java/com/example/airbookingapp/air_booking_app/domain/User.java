package com.example.airbookingapp.air_booking_app.domain;

import jakarta.persistence.*;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Integer userId;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    private String fullName;

    private LocalDate dateOfBirth;

    @Column(columnDefinition = "varchar(255) CHECK(gender IN ('Male', 'Female'))")
    private String gender;

    private String contactPhone;

    private String email;

    private String identityNumber;

    @Column(nullable = false)
    private boolean isAdmin;

    @OneToMany(mappedBy = "user")
    private List<Booking> bookings;

    public User() {
    }

    public User(String username, String encode) {
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIdentityNumber() {
        return identityNumber;
    }

    public void setIdentityNumber(String identityNumber) {
        this.identityNumber = identityNumber;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public List<String> getRoles() {
        List<String> roles = new ArrayList<>();
        if (isAdmin()) {
            roles.add("ADMIN");
        } else {
            roles.add("USER");
        }
        return roles;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }
}