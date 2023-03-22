package com.teamapp.gospy;

import com.redis.om.spring.annotations.EnableRedisDocumentRepositories;
import com.teamapp.gospy.listeners.AppEventListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;


@SpringBootApplication
@Configuration
@EnableRedisDocumentRepositories
public class GoSpyApplication {

	public static void main(String[] args) {
		SpringApplication springApplication =
				new SpringApplication(GoSpyApplication.class);
		springApplication.addListeners(new AppEventListener());
		SpringApplication.run(GoSpyApplication.class, args);
	}

}
