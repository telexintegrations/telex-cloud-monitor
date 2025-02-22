// CloudMonitorApplication.java
package com.cost.cloud.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableScheduling
public class TrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrackerApplication.class, args);
	}

}