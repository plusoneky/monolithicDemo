package com.qq183311108;

import javax.servlet.MultipartConfigElement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FirmwareServerApplication {

//	@Bean
//	MultipartConfigElement multipartConfigElement(){
//		MultipartConfigFactory foctory = new MultipartConfigFactory();
//		foctory.setLocation("/tmp");
//		return foctory.createMultipartConfig();
//	}
	
	
	public static void main(String[] args) {
		SpringApplication.run(FirmwareServerApplication.class, args);
	}
}
