package com.example.travelpointsbackend;

import com.example.travelpointsbackend.model.*;
import com.example.travelpointsbackend.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TravelPointsBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(TravelPointsBackEndApplication.class, args);
	}

}
