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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.type.TypeReference;
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
	public RearMessageBox addRearMessage(@RequestPart("rearMessageInfo") String toString,
//													     @RequestBody RestaurantMessageBox RestaurantMessageBox,
			RearMessageBox rearMessage) throws Exception {
		System.out.println(toString);
		Map<String, String> dispatch = new ObjectMapper().readValue(toString,
				new TypeReference<HashMap<String, String>>() {
				});
		String accountIndex = dispatch.get("userAccount");
		Integer restaurantId = Integer.valueOf(dispatch.get("restaurantId"));

//			Integer score = null;
//			if (dispatch.get("score") != null) {
//				score = Integer.valueOf(dispatch.get("score"));
//			}

		String text = dispatch.get("text");
		UserAccount userAccount = userAccountService.getByName(accountIndex);
//			Restaurant restaurant = restaurantService.findById(restaurantId);

		rearMessage.setMessageText(text);
		rearMessage.setCondition(text); // 放什麼型態?
		rearMessage.setTime(new Date());
		rearMessage.setUserAccount(userAccount);
//			rearMessage.setRestaurant(restaurant);
		RearMessageBox newMessagaBox = rearMessageBoxService.insert(rearMessage);
		return newMessagaBox;
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

}
