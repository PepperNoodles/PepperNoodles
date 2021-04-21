package com.infotran.springboot.shoppingmall.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class checkOutController {

	@GetMapping(value="", produces = { "application/json; charset=UTF-8" })
	public @ResponseBody Map<String,Object>  sendCheckoutProduct (){
		Map<String,Object> map = new HashMap<String,Object>();
		
		return map;
	}
	
	
	
}
