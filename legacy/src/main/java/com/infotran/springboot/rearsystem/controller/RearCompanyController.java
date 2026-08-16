package com.infotran.springboot.rearsystem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.controller.CompanyDetailController;
import com.infotran.springboot.companysystem.service.CompanyDetailService;
import com.infotran.springboot.rearsystem.service.RearUserAccountService;
import com.infotran.springboot.shoppingmall.model.OrderList;

@Controller
@ResponseBody
public class RearCompanyController {
	
	@Autowired
	private RearUserAccountService rearUserAccountService;
	
	@Autowired
	private RearUserAccountService rearCompanyService;
	
	//Insert寫活的
//	@PostMapping("/rearUserAccountInsert.controller")
//	public UserAccount processUserAccountInsert(@RequestBody UserAccount userAccount) {
//		return rearUserAccountService.insert(userAccount);
//	}
	//Insert寫死的
//	@PostMapping("/rearUserAccountInsert1.controller")
//	public UserAccount processUserAccountInsert2() {
//		UserAccount userAccount1 = new UserAccount();
//		userAccount1.setAccountIndex("aaa5311@gmail.com");
//		userAccount1.setPassword("a123456@");
//		userAccount1.setEnabled(true);
//		return rearUserAccountService.insert(userAccount1);
//	}
	
//	@GetMapping("/rearUserAccountDelete.controller")
//	public String processUserAccountDelete(@RequestParam(name="account_id") Integer id) {
//		rearUserAccountService.deleteById(id);
//		return "ok";
//	}
	
//	@GetMapping(value="/rearUserAccountQueryById.controller")
//	public UserAccount processUserAccountQueryById(@RequestParam(name = "account_id") Integer id) {
//		System.out.println("1");
//		UserAccount enabledStatus = rearUserAccountService.finById(id);		
//		//判斷enable改變狀態
//		if(enabledStatus.isEnabled()) {
//			enabledStatus.setEnabled(false);
//		}
//		else {
//			enabledStatus.setEnabled(true);
//		}
//		rearUserAccountService.insert(enabledStatus);
//		System.out.println(enabledStatus.isEnabled());		
//		return enabledStatus;
//		
//	}
	
//	@GetMapping("/rearUserAccountUpdate.controller")
//	public UserAccount processUserAccountUpdate(@RequestParam UserAccount userAccount) {
//		return rearUserAccountService.update(userAccount);
//	}
	
	//找CompanyList
	@GetMapping(value="/rearStage/getCompanyList") //如改成/user/getCompanyList就要登入會員才會顯示資料
	public @ResponseBody Map<String,ArrayList<UserAccount>> findCompanyByUserName(@ModelAttribute("userAccount")UserAccount user){
		Map<String,ArrayList<UserAccount>> mapview = new HashMap<String,ArrayList<UserAccount>>();
		ArrayList<UserAccount>  companyViewList = rearCompanyService.findCompanyUserList();
		mapview.put("CompanyList", companyViewList);
		return mapview;
	}

}
