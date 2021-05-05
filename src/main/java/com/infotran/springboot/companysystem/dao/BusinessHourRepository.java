package com.infotran.springboot.companysystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantBusinHour;

public interface BusinessHourRepository extends JpaRepository<RestaurantBusinHour, Integer> {
	
	//springboot p.119 需使用JPA關鍵字
		public RestaurantBusinHour findByDayAndRestaurant(String day,Restaurant restaurant);
		
		@Query(value="select * from restaurantBusinHour  where fk_restaurant_id=? order by day ",nativeQuery = true)
		List<RestaurantBusinHour> businHourSByRestID(Integer id);
}
