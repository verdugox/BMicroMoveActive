package com.MoveActive.MoveActive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class MoveActiveApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoveActiveApplication.class, args);
	}

}
