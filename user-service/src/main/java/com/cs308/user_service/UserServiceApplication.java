package com.cs308.user_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
// Enables the application to become a Eureka client
@EnableDiscoveryClient
// Enables the functionality for JPA repositories
@EnableJpaRepositories(basePackages = {"com.cs308.user_service.repo"})
public class UserServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserServiceApplication.class, args);
	}

}
