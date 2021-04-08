package com.infotran.springboot.welcome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {


	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/INDEX")
	public String home2() {
		return "index";
	}
	
	
}
