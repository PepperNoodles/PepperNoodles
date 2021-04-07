package com.infotran.springboot.loginsystem.dao;

import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import com.infotran.springboot.loginsystem.model.UserAccount;


@Repository
public interface UserAccountDAOInterface {
	
	public UserAccount SelectByAccount(UserAccount user);

	public List<UserAccount> SelectAll();

	public void DeleteAccount(UserAccount user);
	
	public void evictAccount(UserAccount user);

	//判斷會員是否存在
	public boolean isExist(UserAccount user);
	
	//判斷會員帳號是否重複
	public String checkUsserAccount(UserAccount user);
	
	//註冊會員
	public int RegistureUserAccount(UserAccount user);
	

	public UserAccount Update(int id, String name, String password);

	public Set<UserAccount> selectfriend(UserAccount user);

	public Set<UserAccount> selectuser(UserAccount user);
	

	
}
