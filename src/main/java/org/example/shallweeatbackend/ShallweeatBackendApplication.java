package org.example.shallweeatbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ShallweeatBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShallweeatBackendApplication.class, args);
	}

}