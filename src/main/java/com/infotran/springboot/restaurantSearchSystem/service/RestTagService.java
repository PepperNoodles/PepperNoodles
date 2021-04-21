package com.infotran.springboot.restaurantSearchSystem.service;

import com.infotran.springboot.commonmodel.FoodTag;

public interface RestTagService {
	
	FoodTag findById(Integer id);
	
	FoodTag findByFoodTagName(String tagName);
}
