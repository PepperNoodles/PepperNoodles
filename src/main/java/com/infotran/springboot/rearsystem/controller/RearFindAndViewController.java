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
	
	@GetMapping("/Editproduct")
	public String editproduct() {
		return "shoppingSystem/Editproduct";
	}
	
	//餐廳用的

	@GetMapping("/rest")
	public String rearRestaurant() {
		return "rearStage/rearStageRestaurant";
	}
	
	//聯絡我們
	@GetMapping("/rearContactMessage")
	public String rearContactMessage() {
		return "rearStage/rearContactMessage";
	}
	
	//後台訊息
	@GetMapping("/rearMessage")
	public String rearMessage() {
		return "rearStage/rearMessage";
	}
	
}
