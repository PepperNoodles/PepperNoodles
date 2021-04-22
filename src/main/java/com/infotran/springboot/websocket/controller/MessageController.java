package com.infotran.springboot.websocket.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

import com.infotran.springboot.websocket.model.MessageModel;
import com.infotran.springboot.websocket.storage.UserStorage;



@RestController
@CrossOrigin
public class MessageController {
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	
	//registerStompEndpoint "chat"
	//send message from 1 user to another
	  @MessageMapping("/chat/{to}")
	    public void sendMessage(@DestinationVariable String to, MessageModel message) {
	        System.out.println("handling send message: " + message + " to: " + to);
	        boolean isExists = UserStorage.getInstance().getUsers().contains(to);
			//destination 對應到registerStompEndpoint的configureMessageBroker的enableSimpleBroker
			//為何要加message?
	        if (isExists) {
	        	System.out.println("convertAndSend"+to+":"+message);
	            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
	        }
		
	  }
}