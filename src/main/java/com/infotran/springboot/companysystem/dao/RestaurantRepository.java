package com.infotran.springboot.companysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.commonmodel.Restaurant;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {



}
