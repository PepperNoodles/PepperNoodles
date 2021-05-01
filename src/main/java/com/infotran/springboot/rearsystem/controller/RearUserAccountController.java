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

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.rearsystem.service.RearUserAccountService;
import com.infotran.springboot.shoppingmall.model.OrderList;

@Controller
@ResponseBody
public class RearUserAccountController {
	
	@Autowired
	private RearUserAccountService rearUserAccountService;
	
	//Insert寫活的
	@PostMapping("/rearUserAccountInsert.controller")
	public UserAccount processUserAccountInsert(@RequestBody UserAccount userAccount) {
		return rearUserAccountService.insert(userAccount);
	}
	//Insert寫死的
	@PostMapping("/rearUserAccountInsert1.controller")
	public UserAccount processUserAccountInsert2() {
		UserAccount userAccount1 = new UserAccount();
		userAccount1.setAccountIndex("aaa5311@gmail.com");
		userAccount1.setPassword("a123456@");
		userAccount1.setEnabled(true);
		return rearUserAccountService.insert(userAccount1);
	}
	
	@GetMapping("/rearUserAccountDelete.controller")
	public String processUserAccountDelete(@RequestParam(name="account_id") Integer id) {
		rearUserAccountService.deleteById(id);
		return "ok";
	}
	
	@GetMapping(value="/rearUserAccountQueryById.controller")
	public UserAccount processUserAccountQueryById(@RequestParam(name = "account_id") Integer id) {
		System.out.println("1");
		UserAccount enabledStatus = rearUserAccountService.finById(id);		
		//判斷enable改變狀態
		if(enabledStatus.isEnabled()) {
			enabledStatus.setEnabled(false);
		}
		else {
			enabledStatus.setEnabled(true);
		}
		rearUserAccountService.insert(enabledStatus);
		System.out.println(enabledStatus.isEnabled());		
		return enabledStatus;
		
	}
	
	@GetMapping("/rearUserAccountUpdate.controller")
	public UserAccount processUserAccountUpdate(@RequestParam UserAccount userAccount) {
		return rearUserAccountService.update(userAccount);
	}
	
	//找AccountList
	@GetMapping(value="/user/getAccountList")
	public @ResponseBody Map<String,ArrayList<UserAccount>> findUserAccountListByUserName(@ModelAttribute("userAccount")UserAccount user){
		Map<String,ArrayList<UserAccount>> mapview = new HashMap<String,ArrayList<UserAccount>>();
		ArrayList<UserAccount>  accountViewList = rearUserAccountService.findAccountList();
		mapview.put("AccountList", accountViewList);
		return mapview;
	}

}
