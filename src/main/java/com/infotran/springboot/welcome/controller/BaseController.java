package com.infotran.springboot.welcome.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class BaseController {

	@GetMapping("/")
	public String home() {
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
	
	@GetMapping("/user/websocket")
	public String userMain() {
		return "websocket/index";
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
