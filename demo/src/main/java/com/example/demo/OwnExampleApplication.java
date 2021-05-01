package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.spec.EdDSAParameterSpec;

@SpringBootApplication
public class OwnExampleApplication {

	public static void main(String[] args) {
		SpringApplication.run(OwnExampleApplication.class, args);
	}
}
