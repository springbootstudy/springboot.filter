package com.ctsi.springboot.token;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.ctsi.springboot.token.mydefine.EnableMyService;

@SpringBootApplication
@EnableMyService
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
	
}
