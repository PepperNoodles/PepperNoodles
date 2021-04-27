package com.infotran.springboot.websocket.model.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.websocket.model.SocketMessage;

public interface SocketMessageRepository extends JpaRepository<SocketMessage, Integer> {
	
	@Query(value="from SocketMessage s where s.mainUser = :mainUser and s.toUserAccounts = :toUser "
			+ "	  or s.mainUser = :toUser and s.toUserAccounts = :mainUser")
	List<SocketMessage> findUserMessageLog(UserAccount mainUser,UserAccount toUser,Pageable page);
	
}
