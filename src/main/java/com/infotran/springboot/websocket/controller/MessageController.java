package com.infotran.springboot.websocket.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.userAccsystem.service.UserSysService;
import com.infotran.springboot.websocket.model.MessageModel;
import com.infotran.springboot.websocket.model.SocketMessage;
import com.infotran.springboot.websocket.model.service.SocketMessageServiceImpl;
import com.infotran.springboot.websocket.storage.UserStorage;


@RestController
@CrossOrigin
public class MessageController {
	
	@Autowired
    private SimpMessagingTemplate simpMessagingTemplate;
	
	@Autowired
	private SocketMessageServiceImpl socketMessageServiceImpl;
	
	@Autowired
	private UserSysService userSysService;
	
	//registerStompEndpoint "chat"
	//send message from 1 user to another
	  @MessageMapping("/chat/{to}")
	   public void sendMessage(@DestinationVariable String to, MessageModel message) {
	        System.out.println("handling send message: " + message + " to: " + to);
	        //boolean isExists = UserStorage.getInstance().getUsers().contains(to);
			//destination 對應到registerStompEndpoint的configureMessageBroker的enableSimpleBroker
			//為何要加message?
	        //if (isExists) {
	        	System.out.println("convertAndSend"+to+":"+message);
	        	//要新增到資料表裡面
	        	SocketMessage messageDB = new SocketMessage();
	        	String mainuserId =message.getFromLogin();
	        	String toUserId= message.getTo();
	        	UserAccount mainUser = userSysService.findByAccountIndex(mainuserId);
	        	UserAccount toUser = userSysService.findByAccountIndex(toUserId);
	        	messageDB.setMainUser(mainUser);
	        	messageDB.setToUserAccounts(toUser);
	        
	        	messageDB.setMessage(message.getMessage());
	        	messageDB.setDate(message.getDate());
	        	socketMessageServiceImpl.saveMessage(messageDB);
	        	
	        	////
	            simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
	       // }
		
	  }
	  
	  
	  @GetMapping(value = "/getUserMessage/{userAccount}/{toUser}")
		 public List<MessageModel> getUserMessage(@PathVariable("userAccount") String userAccount,@PathVariable("toUser") String toUserAccount){
			  UserAccount user = userSysService.findByAccountIndex(userAccount);
			  UserAccount toUser = userSysService.findByAccountIndex(toUserAccount);
			  List<SocketMessage> messages = socketMessageServiceImpl.findUserMessageLog(user, toUser);
			  List<MessageModel> messageModels= new ArrayList<MessageModel>();
			  for(SocketMessage soc:messages ) {
				  MessageModel mess = new MessageModel();
				  mess.setFromLogin(soc.getMainUser().getAccountIndex());
				  mess.setTo(soc.getToUserAccounts().getAccountIndex());
				  mess.setDate(soc.getDate());
				 
				  String dbMessage =soc.getMessage().replaceAll("\\n", "");
				 
				  mess.setMessage(dbMessage);
				  
				  messageModels.add(mess);
			  }
			  
			  
			  
			  return messageModels;
		  }
}