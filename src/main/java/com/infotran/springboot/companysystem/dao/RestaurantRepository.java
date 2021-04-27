package com.infotran.springboot.companysystem.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

	//springboot p.119 需使用JPA關鍵字
	public Restaurant findByRestaurantAddress(String RAddress);



}
