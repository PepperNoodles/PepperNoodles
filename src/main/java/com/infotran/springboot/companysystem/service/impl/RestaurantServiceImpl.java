package com.infotran.springboot.companysystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.FoodTag;
import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.dao.RestaurantRepository;
import com.infotran.springboot.companysystem.service.RestaurantService;
import com.infotran.springboot.loginsystem.dao.FoodTagRepository;

@Service
public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	RestaurantRepository restaurantDao;
	
	@Autowired
	FoodTagRepository foodTagRepository;

	@Override
	public Restaurant save(Restaurant restaurant) {
		Restaurant re = restaurantDao.save(restaurant);
		return re;
	}

	@Override
	public void delete(Integer id) {
		restaurantDao.deleteById(id);
	}

	@Override
	public Restaurant get(Integer id) {
		Optional<Restaurant> optional = restaurantDao.findById(id);
		Restaurant resturant = null;

		if (optional.isPresent()) {
			resturant = optional.get();
		} else {
			throw new RuntimeException("餐廳(id=" + id + ")不存在!");
		}
		return resturant;
	}

	@Override
	public void update(Restaurant restaurant) {
		restaurantDao.save(restaurant);

	}

	@Override
	public List<Restaurant> getAllRestaurant() {
		return restaurantDao.findAll();
	}

	@Override
	public Restaurant findByRestaurantAddress(String RAddress) {
		return restaurantDao.findByRestaurantAddress(RAddress);
	}

	@Override
	public Restaurant findById(Integer id) {
		Optional<Restaurant> restDetailOptional = restaurantDao.findById(id);
		Restaurant restDetail = null;
		if (restDetailOptional.isPresent()) {
			restDetail = restDetailOptional.get();
		} else {
			throw new RuntimeException("rest(id=" + id + ")不存在");
		}
		return restDetail;
	}

	@Override
	public List<FoodTag> getAllFoodTag() {
		List<FoodTag> taglist = foodTagRepository.findAll();
		return taglist;
	}



}
