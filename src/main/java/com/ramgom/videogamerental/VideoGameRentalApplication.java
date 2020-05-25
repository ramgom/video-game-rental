package com.ramgom.videogamerental;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication
@EnableJpaRepositories
@EnableCaching
@EnableSwagger2WebMvc
public class VideoGameRentalApplication {

	public static void main(String[] args) {
		SpringApplication.run(VideoGameRentalApplication.class, args);
	}
}
