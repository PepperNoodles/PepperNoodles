package com.infotran.springboot.userAccsystem.repository;

import java.util.ArrayList;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.UserAccount;

public interface UserSysRepository extends JpaRepository<UserAccount, Integer> {
	public UserAccount findByAccountIndex(String accIndex);
	
	
	@Query(value="SELECT dbo.userAccount.acoount_index, dbo.messageBox.text "
				+ "FROM  dbo.messageBox INNER JOIN "
				+ "dbo.userAccount ON dbo.messageBox.fk_userAccount = dbo.userAccount.account_id AND dbo.messageBox.fk_netizen_account = dbo.userAccount.account_id"
				+ "WHERE (dbo.userAccount.acoount_index = ?1)",nativeQuery=true)
	public ArrayList<String> findUserMessagebox(String name);
	
	
}
