package com.infotran.springboot.shoppingmall.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value={"/shoppingSystem","/user/shoppingSystem"})
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
	
	@GetMapping("/checkOutPage")
	public String checkOutPage() {
		return "shoppingSystem/checkOutPage";
	}
	@GetMapping("/confirmOrderAndInvoice")
	public String checkOutconfirmOrderAndInvoice() {
		return "shoppingSystem/confirmOrderAndInvoice";
	}
	@PostMapping("/confirmOrderAndInvoice")
	public String checkOutconfirmOrderAndInvoice2() {
		return "shoppingSystem/confirmOrderAndInvoice";
	}
	@GetMapping("/OrderFormECpay")
	public String checkOutOrderFormECpay() {
		return "shoppingSystem/OrderFormECpay";
	}
	@GetMapping("/NewFile")
	public String checkOutNEWECpay() {
		return "shoppingSystem/NewFile";
	}
//	@PostMapping("/NewFile")
//	public String checkOutNEWECpay2() {
//		return "shoppingSystem/NewFile";
//	}
	
	
	
	
}
