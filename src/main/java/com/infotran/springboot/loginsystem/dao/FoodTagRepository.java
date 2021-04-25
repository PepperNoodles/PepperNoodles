package com.infotran.springboot.loginsystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.Restaurant;

public interface FoodTagRepository extends JpaRepository<FoodTag, Integer> {
	
	FoodTag findByFoodTagName(String name);
	
	List<FoodTag> findByRestaurant(Restaurant restaurant);


}
