package com.example.SmartCare;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class SmartCareApplication {

	public static void main(String[] args) {
		SpringApplication.run(SmartCareApplication.class, args);
	}

}
