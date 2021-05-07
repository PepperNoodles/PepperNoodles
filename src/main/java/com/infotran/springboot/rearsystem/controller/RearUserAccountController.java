package com.infotran.springboot.rearsystem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import com.infotran.springboot.rearsystem.dao.RearSendEmail;
import com.infotran.springboot.rearsystem.service.RearUserAccountService;
import com.infotran.springboot.shoppingmall.model.OrderList;

@Controller
@ResponseBody
public class RearUserAccountController {
	
	@Autowired
	private RearUserAccountService rearUserAccountService;
	
//	@Autowired
//	private RearSendEmail rearSendEmail;
	
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
		RearSendEmail rearSendEmail = new RearSendEmail(); //寄信請關信件防毒軟體
		//判斷enable改變狀態並寄信通知
		if(enabledStatus.isEnabled()) {
			rearSendEmail.sendRightEmail(enabledStatus);
			enabledStatus.setEnabled(false);
		}
		else {
			rearSendEmail.sendRightEmail(enabledStatus);
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
	@GetMapping(value="/rearStage/getAccountList") //如改成/user/getAccountList就要登入會員才會顯示資料
	public @ResponseBody Map<String,ArrayList<UserAccount>> findUserAccountListByUserName(@ModelAttribute("userAccount")UserAccount user){
		Map<String,ArrayList<UserAccount>> mapview = new HashMap<String,ArrayList<UserAccount>>();
		ArrayList<UserAccount>  accountViewList = rearUserAccountService.findAccountList();
		mapview.put("AccountList", accountViewList);
		return mapview;
	}
	
	//取得一般使用者
	@GetMapping(value="/rearStage/getAccountList1") //如改成/user/getAccountList就要登入會員才會顯示資料
	public @ResponseBody Map<String,ArrayList<UserAccount>> findUserAccountListByUserName1(@ModelAttribute("userAccount")UserAccount user){
		Map<String,ArrayList<UserAccount>> mapview = new HashMap<String,ArrayList<UserAccount>>();
		ArrayList<UserAccount>  accountViewList = rearUserAccountService.findAccountUserList();
		mapview.put("AccountList1", accountViewList);
		return mapview;
	}
	
	//模糊搜尋帳號
	@GetMapping(value = "/rearStage/findAccountLike")
	public List<UserAccount> processFindAccountLike(@RequestParam(name = "acoount_index") String accountIndex){
		return rearUserAccountService.findAccountLike("%" + accountIndex + "%");
	}

}
