package com.infotran.springboot.companysystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.RestaurantBusinHour;
import com.infotran.springboot.companysystem.dao.BusinessHourRepository;
import com.infotran.springboot.companysystem.service.BusinessHourService;

@Service
public class BusinessHourServiceImpl implements BusinessHourService {

	@Autowired
	BusinessHourRepository businessHourRepository;	
	
	@Override
	public RestaurantBusinHour save(RestaurantBusinHour restaurantBusinHour) {
		RestaurantBusinHour restHour = businessHourRepository.save(restaurantBusinHour);
		return restHour;
	}





}
