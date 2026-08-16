package com.infotran.springboot.restaurantSearchSystem.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.service.RestaurantService;

@Controller
public class RestPageController {
	
	@Autowired
	RestaurantService restaurantService; 

	@GetMapping("/viewRest/{restId}")
	public String findRestaurnatPage(@PathVariable(name="restId") Integer restId,Model model) {
		Restaurant rest = restaurantService.findById(restId);
		List<MenuDetail> menus = rest.getMenus();
		Set<FoodTag> tags = rest.getFoodTag();
		List<Integer> menuIdCollect = new ArrayList<>();
		List<String> tagCollect = new ArrayList<>();
		
		for(MenuDetail menu:menus) {
			menuIdCollect.add(menu.getMenuDetailId());
		}
		
		for(FoodTag tag:tags) {
			tagCollect.add(tag.getFoodTagName());
		}
		
		
		String menusjson="";
		String tagsjson="";
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		
		try {
			menusjson= ow.writeValueAsString(menuIdCollect);
			tagsjson=ow.writeValueAsString(tagCollect);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		

		model.addAttribute("rest",rest);
		model.addAttribute("menu",menusjson);
		model.addAttribute("tag",tagsjson);
		return "restaurantPage/restaurantPage";
	}
	//測試用
	@GetMapping("/getRest/{restId}")
	@ResponseBody
	public Restaurant getresy(@PathVariable(name="restId") Integer restId,Model model) {
		Restaurant rest = restaurantService.findById(restId);
	
		return rest;
	}
	
	
	
	
}
