package com.infotran.springboot.websocket.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.websocket.model.SocketMessage;
import com.infotran.springboot.websocket.model.repository.SocketMessageRepository;

@Service
public class SocketMessageServiceImpl implements SocketMessageService {

	@Autowired
	SocketMessageRepository socketMessageRepository;
	
	@Override
	public void saveMessage(SocketMessage message) {
		socketMessageRepository.save(message);

	}

	@Override
	public List<SocketMessage> findUserMessageLog(UserAccount mainUser, UserAccount toUser) {
		Pageable pageable = PageRequest.of(0, 100, Sort.Direction.ASC, "date");
		List<SocketMessage> messages = socketMessageRepository.findUserMessageLog(mainUser, toUser,pageable);
		return messages;
	}

}
