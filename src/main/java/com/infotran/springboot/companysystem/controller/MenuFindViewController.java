package com.infotran.springboot.companysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/Rest")
public class MenuFindViewController {
	
	@GetMapping("/menu")
	public String menu() {
		return "company/menu";
	}
	
	@GetMapping("/blog")
	public String blog() {
		return "company/blog";
	}
}
