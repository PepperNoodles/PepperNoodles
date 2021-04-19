package com.infotran.springboot.loginsystem.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/loginSystem")
public class FindViewController {

	@GetMapping("/loginPage")
	public String initPage() {
		return "loginSystem/loginPage";
	}
	
	@GetMapping(value="/membersEdit/{key}",produces = {"application/json"})
	public String editSingleUserFindView(@PathVariable String key,Model model) {
		model.addAttribute("Account",key);
		return "edit/editMember";
	}
	
	@GetMapping(value="/normaluser")
	public String normaluserpage() {
		return "normaluser";
	}
	
	@GetMapping(value="/companyuser")
	public String companyuser() {
		return "companyuser";
	}
	
	@GetMapping(value="/admin")
	public String admin() {
		return "admin";
	}
	
	@GetMapping(value="/403")
	public String error403() {
		return "403";
	}
	
	
	
}
