package com.infotran.springboot.userAccsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.Like;
import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.UserAccount;

public interface LikeRepository extends JpaRepository<Like, Integer> {
	
	
	@Query(value="SELECT l FROM Like l WHERE l.fkUAId = ?1 and l.fkMSNId =?2 ")
	public Like getByUserAccountAndMessageBox(UserAccount userAccount, MessageBox msn);
	


	
	
}
