package com.example.airbookingapp.air_booking_app;

import com.example.airbookingapp.air_booking_app.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
public class AirBookingAppApplication {
	public static void main(String[] args) {
		SpringApplication.run(AirBookingAppApplication.class, args);
	}
//	@Override
//	public void run(String... args) throws Exception {
//		if (userRepository.findByUsername("admin").isEmpty()) {
//			User admin = new User();
//			admin.setUsername("admin");
//			admin.setPassword("1");
//			admin.setAdmin(true);
//			userRepository.save(admin);
//		}
//	}
}
