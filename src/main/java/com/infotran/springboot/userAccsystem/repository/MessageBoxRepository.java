package com.infotran.springboot.userAccsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.UserAccount;

public interface MessageBoxRepository extends JpaRepository<MessageBox, Integer> {
	
	public List<MessageBox> findByUserAccount(UserAccount UserAccount);
	
	@Query(("SELECT m FROM MessageBox m WHERE m.replyMessageBoxes = null"))
	public List<MessageBox> findByUserAccountAndReplyMessageBoxesNull(UserAccount UserAccount);


	
	
}
