package com.infotran.springboot.restaurantMessage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.RestaurantReplyMessage;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.companysystem.service.impl.MenuDetailServiceImpl;
import com.infotran.springboot.loginsystem.service.Impl.UserAccountServiceImpl;
import com.infotran.springboot.restaurantMessage.service.impl.RestaurantMessageBoxServiceImpl;
import com.infotran.springboot.restaurantMessage.service.impl.RestaurantReplyMessageServiceImpl;
import com.infotran.springboot.userAccsystem.service.inplement.UserSysServiceImpl;

@Controller
public class RestaurantMessageController {
	
	@Autowired
	private RestaurantService restaurantService;
	@Autowired
	private MenuDetailServiceImpl menuDetailService;
	@Autowired
	private RestaurantMessageBoxServiceImpl restaurantMessageBoxService;
	@Autowired
	private UserAccountServiceImpl userAccountService;
	@Autowired
	private UserSysServiceImpl uSysServiceImpl;
	@Autowired
	private RestaurantReplyMessageServiceImpl restaurantReplyMessageService;
	
	
	//企業連的評論區
	@GetMapping("/restPage/{restId}")
	public String restaurantMessage(@PathVariable("restId") Integer restId , Model model) {
		Restaurant rest = restaurantService.findById(restId);
		model.addAttribute("rest", rest);
		return "restaurantPage/companyRestaurantMessage";
	}
	
	//一般會員連的評論區
	@GetMapping("/userPage/{restId}")
	public String userMessage(@PathVariable("restId") Integer restId , Model model) {
		Restaurant rest = restaurantService.findById(restId);
		model.addAttribute("rest", rest);
		return "restaurantPage/restaurantPage";
	}
	
	@GetMapping("/restauranMenu/{restId}")
	@ResponseBody
	public List<MenuDetail> restauranMenu(@PathVariable("restId") Integer restId) {
		Restaurant restaurant = restaurantService.findById(restId);
		List <MenuDetail> menus =  menuDetailService.getByRest(restaurant);
		System.out.println("幾張菜單:"+menus.size());
		return menus;
	}
	
	//新增留言
	@PostMapping(value="/addRestaurantMessage")
	@ResponseBody 
	public RestaurantMessageBox addRestaurantMessage(@RequestPart("restMessageInfo")String toString,
//												     @RequestBody RestaurantMessageBox RestaurantMessageBox,
									                 RestaurantMessageBox restMessage) throws Exception{
		System.out.println(toString);
		Map<String, String> dispatch = new ObjectMapper().readValue(toString, new TypeReference<HashMap<String, String>>() {});
		String accountIndex = dispatch.get("netizenAccount");
		Integer restaurantId = Integer.valueOf(dispatch.get("restaurantId"));
		
		Integer score = null;
		if (dispatch.get("score") != null) {
			score = Integer.valueOf(dispatch.get("score"));
		}
	
		
		String text =dispatch.get("text");
		UserAccount netizenAccount = userAccountService.getByName(accountIndex);
		Restaurant restaurant = restaurantService.findById(restaurantId);
		
		restMessage.setText(text);
		restMessage.setScore(score);
		restMessage.setTime(new Date());
		restMessage.setUserAccount(netizenAccount);
		restMessage.setRestaurant(restaurant);
		RestaurantMessageBox newMessagaBox = restaurantMessageBoxService.insert(restMessage);
		//新增餐廳分數
		String avgRank = restaurantMessageBoxService.avgRank(restaurant);

		restaurant.setRankAmount(avgRank);
		restaurantService.update(restaurant);
		return newMessagaBox;
	}
	
	@GetMapping("/allRestaurantMessage/{restId}")
	@ResponseBody
	public List<RestaurantMessageBox> allRestaurantMessage(@PathVariable("restId") Integer restId) {
		System.out.println("找餐廳ID="+restId+"的留言");
		Restaurant restaurant = restaurantService.findById(restId);
		List <RestaurantMessageBox> restMessageBox =  restaurantMessageBoxService.getByRest(restaurant);
		Hibernate.initialize(restMessageBox);
		List<RestaurantMessageBox> restaurantMessageBoxNull = new ArrayList<RestaurantMessageBox>();
		System.out.println("幾則留言:"+restMessageBox.size());
		for(int i =0; i<restMessageBox.size(); i++) {
			restaurantMessageBoxNull.add(restMessageBox.get(i));
	        System.out.println("塞進去後:"+ restaurantMessageBoxNull);
		}
		return restaurantMessageBoxNull;
	}

	//刪除留言deleteMessage
	@GetMapping("/deleteMessage/{messageId}")
	@ResponseBody
	public List<RestaurantMessageBox> deleteMessage(@PathVariable("messageId") Integer MessageId) {
		System.out.println("找刪除的留言ID="+MessageId);
		RestaurantMessageBox restMessage = restaurantMessageBoxService.findById(MessageId);
		Restaurant restaurant = restMessage.getRestaurant();
		restMessage.setRestaurant(null);
		restMessage.setUserAccount(null);
		restaurantMessageBoxService.delete(MessageId);
		//餐廳分數
		String avgRank = restaurantMessageBoxService.avgRank(restaurant);
		restaurant.setRankAmount(avgRank);
		restaurantService.update(restaurant);
		return null;
	}
	
	//刪除回覆留言deleteMessage
		@GetMapping("/deleteReplyMessage/{replyMessageId}")
		@ResponseBody
		public List<RestaurantMessageBox> deleteReplyMessage(@PathVariable("replyMessageId") Integer replyMessageId) {
			System.out.println("找刪除的回覆留言ID="+replyMessageId);
			RestaurantReplyMessage replyMessage = restaurantReplyMessageService.findById(replyMessageId);
			replyMessage.setRestaurantMessageBox(null);
			replyMessage.setUserAccount(null);
			restaurantReplyMessageService.delete(replyMessageId);
			return null;
		}
	
	//回覆留言
	@PostMapping(value="/addReplyRestaurantMessage", consumes= {"application/json"})
	@ResponseBody
	public RestaurantReplyMessage addReplyRestaurantMessage(@RequestBody RestaurantReplyMessage restReplyMessage,
														  	RestaurantReplyMessage replyMessage) {
		//設定留言者
		UserAccount userAccount = uSysServiceImpl.findByAccountIndex(returnNamePath());
		replyMessage.setUserAccount(userAccount);
		//要被留言的留言
		RestaurantMessageBox restMessage = restaurantMessageBoxService.findById(restReplyMessage.getReplyMessageId());
		replyMessage.setRestaurantMessageBox(restMessage);
		replyMessage.setReplyText(restReplyMessage.getReplyText());
		replyMessage.setTime(new Date());
		RestaurantReplyMessage newReply = restaurantReplyMessageService.insert(replyMessage);
		return newReply;
	}
	
	//判斷是否可留
	@PostMapping(value="/judgeCanLeaveMessage")
	@ResponseBody
	public String judgeCanLeaveMessage(@RequestParam String userIndex,@RequestParam Integer restId ) {
		UserAccount userAccount = uSysServiceImpl.findByAccountIndex(userIndex);
		Restaurant restaurant = restaurantService.findById(restId);
		List<RestaurantMessageBox> messages = restaurantMessageBoxService.getByRestAndUser(restaurant, userAccount);
		
		if (messages.size()>2) {
			return "false";
		}else {
			return "true";	
		}		
		
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
	
	@GetMapping(value="/testScore")
	@ResponseBody
	public String getRestScore() {
		Restaurant rest = restaurantService.findById(6);
		
		String avgRank = restaurantMessageBoxService.avgRank(rest);
		
		return avgRank;
	}
		
	
}