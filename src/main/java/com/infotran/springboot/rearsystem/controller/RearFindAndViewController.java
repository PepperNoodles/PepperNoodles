package com.infotran.springboot.rearsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/rearStage")
public class RearFindAndViewController {

	
	@GetMapping("/rearStage")
	public String shoppingMall() {
		return "rearStage/rearStage";
	}
	
	
}
