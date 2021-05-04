package com.infotran.springboot.companysystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infotran.springboot.commonmodel.RestaurantBusinHour;

public interface BusinessHourRepository extends JpaRepository<RestaurantBusinHour, Integer> {
	

}
