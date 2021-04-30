package com.infotran.springboot.restaurantMessage.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.infotran.springboot.commonmodel.MessageBox;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.RestaurantReplyMessage;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.restaurantMessage.service.RestaurantReplyMessageService;
import com.infotran.springboot.restaurantMessage.service.impl.RestaurantMessageBoxServiceImpl;
import com.infotran.springboot.restaurantMessage.service.impl.RestaurantReplyMessageServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.UserSysServiceImpl;

@Controller
public class RestaurantReplyMessageController {
	
	
	@Autowired
	UserSysServiceImpl uSysServiceImpl;
	
	@Autowired
	RestaurantMessageBoxServiceImpl restaurantMessageBoxService;

	@Autowired
	RestaurantReplyMessageService restaurantReplyMessageService;
	
	//新增回覆留言的Ajax方法
		@PostMapping(value="/addReplyRestaurantMessage", consumes= {"application/json"})
		public @ResponseBody String addReplyRestaurantMessage(
												@RequestBody RestaurantReplyMessage restReplyMessage,
												RestaurantReplyMessage replyMessage
												) {
			System.out.println("----------------------");
			//設定留言者
			System.out.println("1:"+uSysServiceImpl.findByAccountIndex(returnNamePath()));
			replyMessage.setUserAccount(uSysServiceImpl.findByAccountIndex(returnNamePath()));
			
			//要被留言的訊息
			System.out.println(restReplyMessage.getReplyMessageId());
			RestaurantMessageBox restMessage = restaurantMessageBoxService.findById(restReplyMessage.getReplyMessageId());
			System.out.println("2:"+restMessage);
			replyMessage.setRestaurantMessageBox(restMessage);
			
			System.out.println("3:"+restReplyMessage.getReplyText());
			replyMessage.setReplyText(restReplyMessage.getReplyText());
			
			System.out.println("4:"+new Date());
			replyMessage.setTime(new Date());
			
			System.out.println("5:"+replyMessage.getRestaurantMessageBox().getRestaurantMessageId());
			restaurantReplyMessageService.insert(replyMessage);
			System.out.println("--------6--------");

			
			return null;
		

		}
		
		
		
		public String returnNamePath() {
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			
			if (!(authentication instanceof AnonymousAuthenticationToken)) {
			    String currentUserName = authentication.getName();
			    System.out.println(currentUserName);
			    return currentUserName;
				}
			System.out.println("no logging user currently!!");
				return null;
		}
		
		
		
		
		
		
		
		
		
}
