package com.sssl.test.springbootcamelrest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.sssl.test.springbootcamelrest")
public class SpringBootCamelRestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootCamelRestApplication.class, args);
	}

}
