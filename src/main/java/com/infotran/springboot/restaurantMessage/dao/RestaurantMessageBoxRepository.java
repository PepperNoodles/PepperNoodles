package com.infotran.springboot.restaurantMessage.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;

public interface RestaurantMessageBoxRepository extends JpaRepository<RestaurantMessageBox, Integer> {
	
	@Query(value="from RestaurantMessageBox r where r.restaurant = :restaurant")
	List<RestaurantMessageBox> getByRest(Restaurant restaurant,Pageable page);
}
