package com.infotran.springboot.rearsystem.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.restaurantSearchSystem.service.RestSearchService;
import com.infotran.springboot.restaurantSearchSystem.service.RestTagService;
import com.infotran.springboot.userAccsystem.service.inplement.UserSysServiceImpl;

@Controller
@RequestMapping(path = "rearStage")
public class RearRestaurantController {

	@Autowired
	private RestSearchService restSearchService;
	
	@Autowired
	private RestTagService restTagService;
	
	@Autowired
	ServletContext servletContext;
	
	@Autowired
	RestaurantService restaurantService; 
	
	@Autowired
	UserSysServiceImpl uSysServiceImpl;
	
	@GetMapping(path = "rearRest/{restId}")
	@ResponseBody
	public Object getSingleRest(@PathVariable Integer restId) {
		Restaurant rest = restaurantService.findById(restId);
		String userIndex="";
		 if (rest.getUserAccount()!=null) {
			 userIndex = rest.getUserAccount().getAccountIndex();
		 }
		 
		HashMap<String, Object> mapWithUser = new HashMap<String, Object>();
		mapWithUser.put("rest",rest);
		mapWithUser.put("userIndex", userIndex);
		
		return mapWithUser;
	}
	
	
	
	@GetMapping(path = "rearRestAll")
	@ResponseBody
	public List<Object> getAllRest() {
		 List<Restaurant> rests = restaurantService.getAllRestaurant();
		 List<Object> restsWithUser = new ArrayList<Object>();
		 String userIndex="";
		 
		 for(Restaurant re:rests) {
			 HashMap<String, Object> restUser = new HashMap<String,Object>();
			 if (re.getUserAccount()!=null) {
				 userIndex = re.getUserAccount().getAccountIndex();				 
			 }
			 restUser.put("userIndex", userIndex);
			 restUser.put("rest",re);
			 restsWithUser.add(restUser);
			 userIndex="";
		 }
		 
		return restsWithUser;
	}
	
	@PutMapping(path = "updateRest")
	@ResponseBody
	public String updateSingleRest(@RequestPart("restinfo")String toString,@RequestPart(name = "restImg",required = false) MultipartFile restImg) {
			
		
		ObjectMapper objectMapper = new ObjectMapper();
		System.out.println(toString);
		String json = toString;
		JsonNode jsonNode=null;
		try {
			jsonNode = objectMapper.readTree(json);
		} catch (JsonProcessingException e) {e.printStackTrace();}
		
		Restaurant rest = restaurantService.findById(jsonNode.get("restaurantId").asInt());
		
		UserAccount user = uSysServiceImpl.findByAccountIndex(jsonNode.get("restaurantOwner").asText());
		if (user!=null) {
			rest.setUserAccount(user);
		}else if(jsonNode.get("restaurantOwner").asText().equals("null")) {
			rest.setUserAccount(null);
		}
		
		
		
		rest.setRestaurantName(jsonNode.get("restaurantName").asText());
		rest.setRestaurantAddress(jsonNode.get("restaurantAddress").asText());
		BigDecimal longitude = new BigDecimal(jsonNode.get("longitude").asText());
		rest.setLongitude(longitude);		
		BigDecimal latitude = new BigDecimal(jsonNode.get("latitude").asText());
		rest.setLatitude(latitude);
		
		rest.setRestaurantContact(jsonNode.get("restaurantContact").asText());
		rest.setRestaurantWebsite(jsonNode.get("restaurantWebsite").asText());
		
		

		if(restImg!=null) {
			
			try {
				
				byte[] restImgByte = restImg.getBytes();
				Blob restImgByteBlob = new SerialBlob(restImgByte);
				rest.setRestaurantPhoto(restImgByteBlob);
				
			} catch (IOException e) {e.printStackTrace();
			} catch (SerialException e) {e.printStackTrace();
			} catch (SQLException e) {e.printStackTrace();}
		}
		
		
		
		restaurantService.save(rest);
		
		
		return "ok";
	}
	
	@GetMapping(path = "/checkCompanyUser")
	@ResponseBody
	public String checkCompanyUser(@RequestParam(name = "userAccount")String userAccount) {
		
		UserAccount user = uSysServiceImpl.findByAccountIndex(userAccount);
		
		if(user!=null) {
			if (user.getCompanyDetail()==null) {
				return "ng";
			}else {
				return "ok";
			}
		}else {
			return "no user";
		}
		
		
		
		
		
	}
	
	
	
}
