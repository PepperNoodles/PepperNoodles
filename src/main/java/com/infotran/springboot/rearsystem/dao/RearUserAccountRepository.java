package com.infotran.springboot.rearsystem.dao;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.shoppingmall.model.OrderList;

public interface RearUserAccountRepository extends JpaRepository<UserAccount, String> {

	@Query(value="select user from UserAccount user")
	public ArrayList<UserAccount> findUserAccountList ();
}
