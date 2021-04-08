package com.infotran.springboot.shoppingmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shoppingSystem")
public class FindViewControllerShop {

	
//	@GetMapping(value="/membersEdit/{key}",produces = {"application/json"})
//	public String editSingleUserFindView(@PathVariable String key,Model model) {
//		model.addAttribute("Account",key);
//		return "edit/editMember";
//	}
	
	@GetMapping("/ShoppingMall")
	public String shoppingMall() {
		return "shoppingSystem/ShoppingMall";
	}
	
}
