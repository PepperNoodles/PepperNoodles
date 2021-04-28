package com.infotran.springboot.welcome.controller;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infotran.springboot.shoppingmall.service.Impl.CancelOrderServiceImpl;

@Controller
public class BaseController {
	
	@Resource
	CancelOrderServiceImpl cancelorderservice;

	@GetMapping("/")
	public String home() {
		cancelorderservice.cancelOrder();
		return "index";
	}

	@GetMapping("/temp")
	public String template() {
		return "template/template_new";
	}

	@GetMapping("/map")
	public String maptest() {
		return "mappage/showmap";
	}
	
	@GetMapping("/backstage")
	public String backtest() {
		return "template/backstageTemp";
	}
	
	@GetMapping("/include")
	public String goInclude() {
		return "includePage/includeNav";
	}

	@GetMapping("/restPage")
	public String template123() {
		return "restaurantPage/restaurantPage";
	}


}
