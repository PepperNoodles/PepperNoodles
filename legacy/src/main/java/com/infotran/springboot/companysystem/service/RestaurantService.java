package com.infotran.springboot.companysystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.Restaurant;

public interface RestaurantService {
	Restaurant save(Restaurant restaurant);
	void delete(Integer id);
	Restaurant get(Integer id);
	void update(Restaurant member);
	List<Restaurant> getAllRestaurant();
	Restaurant findByRestaurantAddress(String RAddress);
	Restaurant findById(Integer id);
	List<FoodTag> getAllFoodTag();
	List<Restaurant> getAllRestaurantByComId(Integer comid);
}
