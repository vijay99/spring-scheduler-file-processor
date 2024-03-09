package com.example.springschedulerfileprocessor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SpringSchedulerFileProcessorApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringSchedulerFileProcessorApplication.class, args);
	}

}
