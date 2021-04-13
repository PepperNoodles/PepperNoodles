package com.infotran.springboot.loginsystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.infotran.springboot.commonmodel.UserAccount;


public interface UserAccountRepository  extends JpaRepository<UserAccount, Integer>{

	//自訂email查詢單一使用者
	@Query("select u FROM UserAccount u Where u.accountIndex= ?1")
	public List<UserAccount> findByAccount(String AccountName);
	
	//自訂email查詢單一使用者
	@Query("select u FROM UserAccount u Where u.accountId= ?1")
	public List<UserAccount> findByAccountId(Integer id);
	
	//自訂布林查詢使用者是否存在
	@Query("select u FROM UserAccount u Where u.accountIndex= ?1")
	public UserAccount isExist(String AccountName);
}
