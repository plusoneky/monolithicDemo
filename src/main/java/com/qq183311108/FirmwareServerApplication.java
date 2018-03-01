package com.qq183311108;

import javax.servlet.MultipartConfigElement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;

import com.qq183311108.config.properties.BizProperties;

@SpringBootApplication
public class FirmwareServerApplication {

	@Autowired
	private BizProperties bizProperties;
	
	@Bean
	MultipartConfigElement multipartConfigElement(){
		MultipartConfigFactory foctory = new MultipartConfigFactory();
		foctory.setLocation(bizProperties.getUploadFileTmpDir());
		return foctory.createMultipartConfig();
	}    
	
	
	public static void main(String[] args) {
		SpringApplication.run(FirmwareServerApplication.class, args);
	}
}
