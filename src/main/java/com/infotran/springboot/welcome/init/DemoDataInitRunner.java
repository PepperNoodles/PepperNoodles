package com.infotran.springboot.welcome.init;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=2)
public class DemoDataInitRunner implements CommandLineRunner {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public DemoDataInitRunner() {
	}
	

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Hello, World");
	}

}
