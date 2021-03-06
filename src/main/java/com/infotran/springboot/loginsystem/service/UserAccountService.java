package com.infotran.springboot.loginsystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.loginsystem.dao.UserAccountRepository;


public interface UserAccountService {
	


	// 存檔
	public Integer save(UserAccount user);

	void delete(UserAccount user);

	// 自訂查詢
	public UserAccount findbyAccount(String AccountName);
	
	public String checkUserAccount(String AccountName);

	// 自訂查詢使用者是否存在
	public UserAccount isExist(String AccountName);

	void update(UserAccount user);

	List<UserAccount> getAllMembers();

}
