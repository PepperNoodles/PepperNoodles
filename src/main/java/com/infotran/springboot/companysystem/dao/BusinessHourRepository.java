package com.infotran.springboot.companysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantBusinHour;

public interface BusinessHourRepository extends JpaRepository<RestaurantBusinHour, Integer> {
	
	//springboot p.119 需使用JPA關鍵字
		public RestaurantBusinHour findByDayAndRestaurant(String day,Restaurant restaurant);
}
