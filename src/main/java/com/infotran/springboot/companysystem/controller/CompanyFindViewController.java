package com.infotran.springboot.companysystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.infotran.springboot.commonmodel.CompanyDetail;
import com.infotran.springboot.companysystem.service.CompanyDetailService;

@Controller
@RequestMapping("/Company")
public class CompanyFindViewController {
	
	
	@Autowired
	private CompanyDetailService comDetailService;

	/**顯示所有會員資料**/ 
	@GetMapping("/showAllComs")
	public String list(Model model) {
		model.addAttribute("companys", comDetailService.getAllCompanys());
		return "company/showAllCompany";
	}
	
	/**顯示會員資料**/ 
	@GetMapping("/showCompany/{comId}")
	public String showCompany(@PathVariable("comId") Integer comId , CompanyDetail comDetail , Model model) {
		comDetail = comDetailService.findById(comId);
		model.addAttribute("comDetail", comDetail);
		return "company/showCompany";
	}
	
	/**登入後的企業畫面**/ 
	@GetMapping("/company/{comId}")
	public String company(@PathVariable("comId") Integer comId , CompanyDetail comDetail , Model model) {
		comDetail = comDetailService.findById(comId);
		model.addAttribute("comDetail", comDetail);
		return "company/company";
	}
	
	@GetMapping("/menu")
	public String menu() {
		return "company/menu";
	}
	
	@GetMapping("/blog")
	public String blog() {
		return "company/blog";
	}
	
	@GetMapping("/left")
	public String left() {
		return "company/left";
	}
}
