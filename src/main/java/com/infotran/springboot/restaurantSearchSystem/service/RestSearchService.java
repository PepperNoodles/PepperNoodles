package com.infotran.springboot.restaurantSearchSystem.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.infotran.springboot.commonmodel.Restaurant;

public interface RestSearchService {
	Restaurant getById(Integer id);
	List<Restaurant> findAll();
	List<Restaurant> findRestaurantNameLike(String restName);
	List<Restaurant> findAddressNameLike(String restName);
	List<Restaurant> findNameAndTag(String restName,String tagName);
	
	List<Restaurant> findNearMap(BigDecimal smallLat,BigDecimal bigLat,BigDecimal smallLong,BigDecimal bigLong);
	List<Restaurant> findNearMapLat(BigDecimal smallLat,BigDecimal bigLat);
	
	List<Restaurant> findSearchNameAndTag(String restName,String tagName);
	List<Restaurant> findASearchtNameAndDistName(String restName,String distName);
	List<Restaurant> findSearchNameAndDistAndTag(String restName,String distName,String tagName);
}
