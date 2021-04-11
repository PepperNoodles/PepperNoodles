package com.infotran.springboot.welcome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BaseController {


	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/temp")
	public String template() {
		return "template/template";
	}
	
	@GetMapping("/map")
	public String maptest() {
		return "mappage/showmap";
	}
	
	@GetMapping("/rest")
	public String company() {
		return "companySystem/restaurantCRUD";
	}

}
