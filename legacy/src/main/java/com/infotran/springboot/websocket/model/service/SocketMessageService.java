package com.infotran.springboot.websocket.model.service;

import java.util.List;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.websocket.model.SocketMessage;

public interface SocketMessageService {
	public void saveMessage(SocketMessage message);
	List<SocketMessage> findUserMessageLog(UserAccount mainUser,UserAccount toUser);
}
