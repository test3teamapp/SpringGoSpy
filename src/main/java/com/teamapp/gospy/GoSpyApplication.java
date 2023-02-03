package com.teamapp.gospy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@EnableRedisRepositories()
@SpringBootApplication
public class GoSpyApplication {

	public static void main(String[] args) {
		SpringApplication.run(GoSpyApplication.class, args);
	}

}
