package com.infotran.springboot.companysystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantBusinHour;
import com.infotran.springboot.companysystem.dao.BusinessHourRepository;
import com.infotran.springboot.companysystem.service.BusinessHourService;
import com.infotran.springboot.companysystem.service.RestaurantService;

@Service
public class BusinessHourServiceImpl implements BusinessHourService {

	@Autowired
	BusinessHourRepository businessHourRepository;	
	
	@Autowired
	RestaurantService restaurantService; 
	
	
	@Override
	public RestaurantBusinHour save(RestaurantBusinHour restaurantBusinHour) {
		RestaurantBusinHour restHour = businessHourRepository.save(restaurantBusinHour);
		return restHour;
	}

	@Override
	public RestaurantBusinHour findByDayAndRestaurantId(String day, Integer id) {
		Restaurant rest = restaurantService.get(id);
		
		RestaurantBusinHour restaurantBusinHour = businessHourRepository.findByDayAndRestaurant(day, rest);
		return restaurantBusinHour;
	}

	@Override
	public void update(RestaurantBusinHour restaurantBusinHour) {
		businessHourRepository.save(restaurantBusinHour);
		
	}





}
