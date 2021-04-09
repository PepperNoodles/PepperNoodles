package com.infotran.springboot.companysystem.service;

import java.util.List;


import com.infotran.springboot.commonmodel.Restaurant;

public interface RestaurantService {
	Restaurant save(Restaurant restaurant);
	void delete(Integer id);
	Restaurant get(Integer id);
	void update(Restaurant member);
	List<Restaurant> getAllRestaurant();
}