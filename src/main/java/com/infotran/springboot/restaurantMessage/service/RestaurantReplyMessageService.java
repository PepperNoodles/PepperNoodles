package com.infotran.springboot.restaurantMessage.service;

import java.util.List;

import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.RestaurantReplyMessage;

public interface RestaurantReplyMessageService {
	
	RestaurantReplyMessage insert(RestaurantReplyMessage restaurantReplyMessage);
	RestaurantReplyMessage update(RestaurantReplyMessage restaurantReplyMessage);
	void delete(Integer id);
	RestaurantReplyMessage findById(Integer id);
	
	List<RestaurantReplyMessage> getByRestMessageBox(RestaurantMessageBox restMessageBox);
}
