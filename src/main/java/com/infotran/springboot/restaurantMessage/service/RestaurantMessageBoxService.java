package com.infotran.springboot.restaurantMessage.service;

import java.util.List;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.UserAccount;

public interface RestaurantMessageBoxService {
	
	RestaurantMessageBox insert(RestaurantMessageBox restaurantMessageBox);
	RestaurantMessageBox update(RestaurantMessageBox restaurantMessageBox);
	void delete(Integer id);
	RestaurantMessageBox findById(Integer id);
	
	List<RestaurantMessageBox> getByRest(Restaurant rest);
	List<RestaurantMessageBox> getByRestAndUser(Restaurant restaurant,UserAccount user);
}
