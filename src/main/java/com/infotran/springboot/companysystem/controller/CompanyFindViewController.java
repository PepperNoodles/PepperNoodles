package com.infotran.springboot.companysystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.infotran.springboot.commonmodel.CompanyDetail;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.CompanyDetailService;
import com.infotran.springboot.userAccsystem.service.UserSysService;

@Controller
@SessionAttributes(names = {"comDetail"})
@RequestMapping("/Company")
public class CompanyFindViewController {
	
	@Autowired
	private UserSysService userSysService;
	
	
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
	
	/**登入後的企業畫面by index**/ 
	@GetMapping("/login/company")
	@ResponseBody
	public String company(Model model) {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		  String currentUserName="";
		  if (!(authentication instanceof AnonymousAuthenticationToken)) {
		      currentUserName = authentication.getName();
		      System.out.println(currentUserName);
				UserAccount user = userSysService.findByAccountIndex(currentUserName);
				CompanyDetail comDetail = user.getCompanyDetail();
				model.addAttribute("comDetail", comDetail);
		      return currentUserName;
		   }
		  System.out.println("no logging user currently!!");
		   return null;
		 }
		
	/**登入後的企業畫面**/
	@GetMapping("/company")
	public String companyloginByIndex(Model model) {
		return "company/company";
	}
	

	/**登入後的企業畫面**/
	@GetMapping("/company/{comId}")
	public String companylogin(@PathVariable("comId") Integer comId , CompanyDetail comDetail , Model model) {
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
