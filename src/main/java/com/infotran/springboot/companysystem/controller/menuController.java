package com.infotran.springboot.companysystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rest")
public class menuController {
	
	@GetMapping("/menu")
	public String menu() {
		return "company/addMenu";
	}

}
