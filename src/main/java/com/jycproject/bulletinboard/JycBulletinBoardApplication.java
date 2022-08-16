package com.jycproject.bulletinboard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@ConfigurationPropertiesScan
@SpringBootApplication
public class JycBulletinBoardApplication {

	public static void main(String[] args) {
		SpringApplication.run(JycBulletinBoardApplication.class, args);
	}

}
