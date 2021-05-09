package com.infotran.springboot.rearMessage.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import org.springframework.web.bind.annotation.RequestParam;
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
import com.infotran.springboot.rearsystem.dao.RearSendEmail;
import com.infotran.springboot.rearsystem.service.RearUserAccountService;
import com.infotran.springboot.shoppingmall.util.inform;

import lombok.Getter;
//import sun.print.resources.serviceui;

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
		rearMessage.isCondition();
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
	

	
//	user(entity) ====>你要任何東西getter、setter
	
//	
	
//	@GetMapping
//	public List<inform> informlist getinformlist() {
//		RearMessageBox rearmessbox  = xxx.get();
//		String message  = rearmessbox.getMessageText();//kitty
//		id  =rearmessbox.getUserAccountId();//id
//		
//		useraccount user = serviceui.getaccountbyid(id);//1個user
//		String index = user.getaacountindex();//user 的帳號
//		
//		infrom inform1 = new inform();
//		inform1.setText(message);
//		inform1.setName(index);
//		
//		List<inform> inform1list = new ArrayList<>();//空的
//		inform1list.add(inform1);//1個infrom物件====>>>kitty 帳號
//		return inform1list ;
//	}
	
	
	
	

	//找MessageList
	@GetMapping(value = "/rearStage/getMessageList")
	public @ResponseBody Map<String,ArrayList<RearMessageBox>> findMessageByUser(@ModelAttribute("rearMessageBox")RearMessageBox message){
		Map<String,ArrayList<RearMessageBox>> mapview = new HashMap<String,ArrayList<RearMessageBox>>();
		ArrayList<RearMessageBox> messageViewList = rearMessageBoxService.findMessage();
		mapview.put("MessageList", messageViewList);
		return mapview;
	}
	

	//找訊息與處理狀態並更新時間
	@GetMapping(value = "/rearMessageFindById")
	@ResponseBody
	public RearMessageBox messageFindById(@RequestParam(name = "rearMessage_id") Integer id) {
		RearMessageBox conditionStatus = rearMessageBoxService.findById(id);

		if(conditionStatus.isCondition()) {

		conditionStatus.setCondition(false);
	}
	else {

		conditionStatus.setCondition(true);
		conditionStatus.setUpdateTime(new Date());
	}
	rearMessageBoxService.insert(conditionStatus);
	return conditionStatus;
	}

	//後台訊息通知
	@GetMapping(value = "/informMessageCondition")
	public @ResponseBody Map<String,List<RearMessageBox>> informMessageCondition(){
		Calendar calendar = new GregorianCalendar(); 
		Date now = new Date();
		calendar.setTime(now);
		Date todayTime = calendar.getTime();
		calendar.add(Calendar.DATE, -3);
		Date replyTime = calendar.getTime();
		SimpleDateFormat datestr = new SimpleDateFormat("yyyy/MM/dd");
		String tTime = datestr.format(todayTime);
		String rTime = datestr.format(replyTime);
		List<RearMessageBox> messageNewList = rearMessageBoxService.findNewMessageByTime(tTime);
		List<RearMessageBox> messageReplyList = rearMessageBoxService.findUpdatedMessageByTime(rTime);
		Map<String,List<RearMessageBox>> mmap = new HashMap<>();
		mmap.put("replyList", messageReplyList);
		mmap.put("newList", messageNewList);
		return mmap;
	}

}
