package com.soundbar91.retrospect_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class RetrospectProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(RetrospectProjectApplication.class, args);
	}

}
