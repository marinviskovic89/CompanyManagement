package com.CompanyManagement;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CompanyManagementApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(CompanyManagementApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
	}
}

