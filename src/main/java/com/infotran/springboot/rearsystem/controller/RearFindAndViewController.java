package com.infotran.springboot.rearsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/rearStage","/user/rearStage"})
public class RearFindAndViewController {

	
	@GetMapping("/indexRearStage")
	public String shoppingMall() {
		return "rearStage/indexRearStage";
	}
	
	@GetMapping("/userAccountRearStage")
	public String rearsystem() {
		return "rearStage/userAccountRearStage";
	}
	

	@GetMapping("/rearStage2")
	public String test() {
		return "rearStage/rearStage2";
	}

	@GetMapping("/rearStage")
	public String chrisRearStage() {
		return "rearStage/rearStage";
	}
	
	
}
