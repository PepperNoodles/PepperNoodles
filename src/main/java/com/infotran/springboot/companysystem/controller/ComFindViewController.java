package com.infotran.springboot.companysystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.infotran.springboot.companysystem.service.CompanyDetailService;

@Controller
public class ComFindViewController {
	
	@Autowired
	private CompanyDetailService comDetailService;

	// 顯示所有會員資料
	@GetMapping("/showAllComs")
	public String list(Model model) {
		model.addAttribute("companys", comDetailService.getAllCompanys());
		return "company/showAllCompany";
	}
}
