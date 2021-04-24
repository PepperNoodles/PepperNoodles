package com.infotran.springboot.restaurantSearchSystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.loginsystem.dao.FoodTagRepository;
import com.infotran.springboot.restaurantSearchSystem.service.RestTagService;

@Service
public class RestTagServiceImpl implements RestTagService {

	@Autowired
	FoodTagRepository foodTagRepository;
	
	@Override
	public FoodTag findById(Integer id) {
		Optional<FoodTag> tagOption = foodTagRepository.findById(id);	
		if (tagOption.isPresent()) {
			FoodTag foodtag = tagOption.get();		
			return foodtag;
		}
		return null;
	}
	
	public List<FoodTag> findAll() {
		List<FoodTag> allTag = foodTagRepository.findAll();
		return allTag;
	}

	@Override
	public FoodTag findByFoodTagName(String tagName) {
		FoodTag foodtag = foodTagRepository.findByFoodTagName(tagName);
		return foodtag;
	}
	

}
