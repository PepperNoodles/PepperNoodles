package com.infotran.springboot.companysystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantBusinHour;

public interface BusinessHourService {

	void update(RestaurantBusinHour restaurantBusinHour);
	RestaurantBusinHour save(RestaurantBusinHour restaurantBusinHour);
	
	RestaurantBusinHour findByDayAndRestaurantId(String day,Integer id);
	
	List<RestaurantBusinHour> businHourSByRestID(Integer id);
	
	void deleteByRestID (Integer id);
	
	List<RestaurantBusinHour> findRestaurantBusinHourIdByRestaurantId(Integer id);
	void deleteByID(Integer id);
}
