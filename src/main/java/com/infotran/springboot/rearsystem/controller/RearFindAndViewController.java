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
	

	@GetMapping("/userAccountRearTest")
	public String test() {
		return "rearStage/userAccountRearTest";
	}
	
	//一般會員
	@GetMapping("/rearNormalUser")
	public String rearNormalUser() {
		return "rearStage/userAccountRearNormalUser";
	}
	//企業會員
	@GetMapping("/userAccountRearCompanyUser")
	public String rearCompanyUser() {
		return "rearStage/userAccountRearCompanyUser";
	}
	//後台首頁
	@GetMapping("/indexRearStage1")
	public String index() {
		return "rearStage/indexRearStage1";
	}
	

	@GetMapping("/rearStage")
	public String chrisRearStage() {
		return "rearStage/rearStage";
	}
	
	
}
