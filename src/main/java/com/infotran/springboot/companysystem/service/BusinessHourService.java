package com.infotran.springboot.companysystem.service;

import com.infotran.springboot.commonmodel.RestaurantBusinHour;

public interface BusinessHourService {

	void update(RestaurantBusinHour restaurantBusinHour);
	RestaurantBusinHour save(RestaurantBusinHour restaurantBusinHour);
	
	RestaurantBusinHour findByDayAndRestaurantId(String day,Integer id);
}
