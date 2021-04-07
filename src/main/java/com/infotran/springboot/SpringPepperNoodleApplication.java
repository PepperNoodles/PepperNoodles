package com.infotran.springboot;

import java.io.File;

import javax.servlet.Filter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import com.infotran.springboot.loginsystem.controller.UserAccountController;

@SpringBootApplication
public class SpringPepperNoodleApplication {

	public static void main(String[] args) {
		
//		new File(UserAccountController.uploadDirectory).mkdir();
		
		SpringApplication.run(SpringPepperNoodleApplication.class, args);
	}

	@Bean 
	FilterRegistrationBean<Filter>  hiddenHttpMethodFilter(){
		FilterRegistrationBean<Filter>  filterBean = new FilterRegistrationBean<>();
		filterBean.setFilter(new HiddenHttpMethodFilter());
		return filterBean;
	}
	
	
	
}
