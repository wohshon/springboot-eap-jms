package com.demo.app2;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
//@SpringBootApplication(exclude = JmsAutoConfiguration.class)
//@EnableJms
//@ComponentScan(basePackages = "com.demo")
public class App2Application extends SpringBootServletInitializer{

	public static void main(String[] args) {
		SpringApplication.run(App2Application.class, args);
	}

}
