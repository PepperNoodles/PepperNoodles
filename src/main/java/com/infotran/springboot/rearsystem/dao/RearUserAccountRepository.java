package com.infotran.springboot.rearsystem.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.shoppingmall.model.OrderList;

public interface RearUserAccountRepository extends JpaRepository<UserAccount, String> {
    
	//取得所有使用者
	@Query(value="select user from UserAccount user")
	public ArrayList<UserAccount> findUserAccountList ();
	
	//取得UserAccount的fk_companyDetailId = null 只抓一般使用者
	@Query(value="select user from UserAccount user where user.companyDetail = null")
	public ArrayList<UserAccount> findAccountUserList ();
	
//	//取得UserAccount的fk_companyDetailId
//	@Query(value="select user from UserAccount user where user.companyDetail.companyDetailId=?1")
//	public ArrayList<UserAccount> findCompanyList (Integer companyDetailId);
	
	//取得UserAccount的fk_companyDetailId
	@Query(value="select user from UserAccount user where user.companyDetail != null")
	public ArrayList<UserAccount> findCompanyUserList ();
	
	
}
