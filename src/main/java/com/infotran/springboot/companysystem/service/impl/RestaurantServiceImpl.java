package com.infotran.springboot.companysystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.dao.RestaurantRepository;
import com.infotran.springboot.companysystem.service.RestaurantService;

public class RestaurantServiceImpl implements RestaurantService {

	@Autowired
	RestaurantRepository restaurantDao;

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

}
