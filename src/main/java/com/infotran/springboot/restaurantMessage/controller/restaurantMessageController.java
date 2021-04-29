package com.infotran.springboot.restaurantMessage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.companysystem.service.impl.MenuDetailServiceImpl;

@Controller
public class restaurantMessageController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private MenuDetailServiceImpl MenuDetailService;
	
	@GetMapping("/restPage/{restId}")
	public String restaurantMessage(@PathVariable("restId") Integer restId , Model model) {
		Restaurant rest = restaurantService.findById(restId);
		model.addAttribute("rest", rest);
		return "restaurantPage/restaurantPage";
	}
	
	@GetMapping("/restauranMenu/{restId}")
	@ResponseBody
	public List<MenuDetail> restauranMenu(@PathVariable("restId") Integer restId , Model model) {
		Restaurant restaurant = restaurantService.findById(restId);
		List <MenuDetail> menus =  MenuDetailService.getByRest(restaurant);
		System.out.println(menus.size());
		return menus;
	}
}
