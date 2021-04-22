package com.infotran.springboot.personalpagesystem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infotran.springboot.commonmodel.UserAccount;

@Controller
@RequestMapping("/personalPage")
public class PersonalEditController {

	@GetMapping(value = "/edit")
	public String checkUserAccount(){

		return "personalPage";
	}
	
	@PostMapping(value = "/saveMessageBox")
	public String saveMessageBox(){
		
		return "personalPage";
	}
	
	
}
