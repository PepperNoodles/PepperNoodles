package com.infotran.springboot.loginsystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.FoodTag;

public interface FoodTagRepository extends JpaRepository<FoodTag, Integer> {
	
	FoodTag findByFoodTagName(String name);


}
