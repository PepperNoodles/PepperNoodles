package com.infotran.springboot.rearMessage.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.RearMessageBox;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.loginsystem.service.Impl.UserAccountServiceImpl;
import com.infotran.springboot.rearMessage.service.impl.RearMessageBoxServiceImpl;
import com.infotran.springboot.rearsystem.service.RearUserAccountService;

@Controller
public class RearMessageController {
	
	@Autowired
	private RearMessageBoxServiceImpl rearMessageBoxService;
	@Autowired
	private RearUserAccountService rearUserAccountService;	
	@Autowired
	private UserAccountServiceImpl userAccountService;
	@Autowired
	private RestaurantService restaurantService;
	
	//新增留言
	@PostMapping(value = "/addRearMessage")
	@ResponseBody
	public RearMessageBox addRearMessage(@RequestPart("rearMessageInfo") String toString) throws Exception {
		
		System.out.println(toString);
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode jsonNode=null;
		try {
			jsonNode = objectMapper.readTree(toString);
		} catch (JsonProcessingException e) {e.printStackTrace();}
		
		String account = jsonNode.get("userAccountIndex").asText();
		UserAccount user = userAccountService.findbyAccount(account);
		String text = jsonNode.get("messageText").asText();
		
		RearMessageBox rearMessage = new RearMessageBox();
		
		rearMessage.setTime(new Date());
		rearMessage.setCondition("N");
		rearMessage.setMessageText(text);
		rearMessage.setUserAccount(user);
		rearMessageBoxService.insert(rearMessage);
//		Map<String, String> dispatch = new ObjectMapper().readValue(toString,
//				new TypeReference<HashMap<String, String>>() {
//				});
//		
//		String accountIndex = dispatch.get("userAccount");
//		Integer restaurantId = Integer.valueOf(dispatch.get("restaurantId"));

//			Integer score = null;
//			if (dispatch.get("score") != null) {
//				score = Integer.valueOf(dispatch.get("score"));
//			}

//		String text = dispatch.get("text");
//		UserAccount userAccount = userAccountService.getByName(accountIndex);
//			Restaurant restaurant = restaurantService.findById(restaurantId);
//
//		rearMessage.setMessageText(text);
//		rearMessage.setCondition("N"); // 放什麼型態?
//		rearMessage.setTime(new Date());
//		rearMessage.setUserAccount(userAccount);
//			rearMessage.setRestaurant(restaurant);
//		RearMessageBox newMessagaBox = rearMessageBoxService.insert(rearMessage);
		return rearMessage; //要再改寫跳轉訊息
	}
	
	@GetMapping("/allRearMessage/{userId}")
	@ResponseBody
	public List<RearMessageBox> allRestaurantMessage(@PathVariable("userId") Integer userId) {
		System.out.println("找帳號ID="+userId+"的留言");
		UserAccount userAccount = rearUserAccountService.finById(userId);
		List <RearMessageBox> rearMessageBox =  rearMessageBoxService.getByUserAccount(userAccount);
		Hibernate.initialize(rearMessageBox);
		List<RearMessageBox> rearMessageBoxNull = new ArrayList<RearMessageBox>();
		System.out.println("幾則留言:"+rearMessageBox.size());
		for(int i =0; i<rearMessageBox.size(); i++) {
			rearMessageBoxNull.add(rearMessageBox.get(i));
	        System.out.println("塞進去後:"+ rearMessageBoxNull);
		}
		return rearMessageBoxNull;
	}
	
	//找MessageList
	@GetMapping(value = "/rearStage/getMessageList")
	public @ResponseBody Map<String,ArrayList<RearMessageBox>> findMessageByUser(@ModelAttribute("rearMessageBox")RearMessageBox message){
		Map<String,ArrayList<RearMessageBox>> mapview = new HashMap<String,ArrayList<RearMessageBox>>();
		ArrayList<RearMessageBox> messageViewList = rearMessageBoxService.findMessage();
		mapview.put("MessageList", messageViewList);
		return mapview;
	}

}
