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
import org.springframework.web.bind.annotation.ResponseBody;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.rearsystem.service.RearUserAccountService;
import com.infotran.springboot.shoppingmall.model.OrderList;

@Controller
@ResponseBody
public class RearUserAccountController {
	
	@Autowired
	private RearUserAccountService rearUserAccountService;
	
	@PostMapping("/rearUserAccountInsert.controller")
	public UserAccount processUserAccountInsert(@RequestBody UserAccount userAccount) {
		return rearUserAccountService.insert(userAccount);
	}
	
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
	
	@GetMapping("/rearUserAccountQueryById.controller")
	public UserAccount processUserAccountQueryById(@RequestParam(name = "account_id") Integer id) {
		return rearUserAccountService.finById(id);
	}
	
	public UserAccount processUserAccountupdate(@RequestParam UserAccount userAccount) {
		return rearUserAccountService.update(userAccount);
	}
	
	//找Account
	@GetMapping(value="/user/getAccountList")
	public @ResponseBody Map<String,ArrayList<UserAccount>> findUserAccountListByUserName(@ModelAttribute("userAccount")UserAccount user){
		Map<String,ArrayList<UserAccount>> mapview = new HashMap<String,ArrayList<UserAccount>>();
		ArrayList<UserAccount>  accountViewList = rearUserAccountService.findAccountList();
		mapview.put("AccountList", accountViewList);
		return mapview;
	}

}
