package com.infotran.springboot.restaurantSearchSystem.service;

import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Pageable;

import com.infotran.springboot.commonmodel.Restaurant;

public interface RestSearchService {
	Restaurant getById(Integer id);
	List<Restaurant> findAll();
	List<Restaurant> findRestaurantNameLike(String restName);
	List<Restaurant> findAddressNameLike(String restName);
	public List<Restaurant> findNameAndTag(String restName,String tagName);
}
