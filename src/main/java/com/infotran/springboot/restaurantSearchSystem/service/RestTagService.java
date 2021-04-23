package com.infotran.springboot.restaurantSearchSystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.FoodTag;

public interface RestTagService {
	
	FoodTag findById(Integer id);
	
	FoodTag findByFoodTagName(String tagName);
	
	List<FoodTag> findAll();
}
