package com.cstapin;

import com.cstapin.auth.properties.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({JwtProperties.class})
public class CsTapInApplication {

	public static void main(String[] args) {
		SpringApplication.run(CsTapInApplication.class, args);
	}

}
