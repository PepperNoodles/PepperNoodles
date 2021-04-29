package com.infotran.springboot.restaurantMessage.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.companysystem.service.impl.MenuDetailServiceImpl;
import com.infotran.springboot.loginsystem.service.Impl.UserAccountServiceImpl;
import com.infotran.springboot.restaurantMessage.service.impl.RestaurantMessageBoxServiceImpl;

@Controller
public class restaurantMessageController {
	
	@Autowired
	private RestaurantService restaurantService;
	
	@Autowired
	private MenuDetailServiceImpl menuDetailService;
	
	@Autowired
	private RestaurantMessageBoxServiceImpl restaurantMessageBoxService;
	
	@Autowired
	private UserAccountServiceImpl userAccountService;
	
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
		List <MenuDetail> menus =  menuDetailService.getByRest(restaurant);
		System.out.println("幾張菜單:"+menus.size());
		return menus;
	}
	
	//新增留言
	@PostMapping(value="/addRestaurantMessage")
	@ResponseBody 
	public RestaurantMessageBox addNewCommentAjax(@RequestPart("restMessageInfo")String toString,
//												  @RequestBody RestaurantMessageBox RestaurantMessageBox,
									              RestaurantMessageBox restMessage , Model model) throws Exception{
		System.out.println(toString);
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		String accountIndex = dispatch.get("netizenAccount");
		Integer restaurantId = Integer.valueOf(dispatch.get("restaurantId"));
		String text =dispatch.get("text");
		UserAccount netizenAccount = userAccountService.getByName(accountIndex);
		Restaurant restaurant = restaurantService.findById(restaurantId);
		
		restMessage.setText(text);
		restMessage.setTime(new Date());
		restMessage.setUserAccount(netizenAccount);
		restMessage.setRestaurant(restaurant);
		RestaurantMessageBox newMessagaBox = restaurantMessageBoxService.insert(restMessage);
		return newMessagaBox;
	}
	
	@GetMapping("/allRestaurantMessage/{restId}")
	@ResponseBody
	public List<RestaurantMessageBox> allRestaurantMessage(@PathVariable("restId") Integer restId , Model model) {
		Restaurant restaurant = restaurantService.findById(restId);
		List <RestaurantMessageBox> restaurantMessageBox =  restaurantMessageBoxService.getByRest(restaurant);
		System.out.println("幾則留言:"+restaurantMessageBox.size());
		return restaurantMessageBox;
	}
}
