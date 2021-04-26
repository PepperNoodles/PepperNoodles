package com.infotran.springboot.companysystem.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.EventList;
import com.infotran.springboot.commonmodel.Restaurant;

public interface EventListRepository extends JpaRepository<EventList, Integer> {
	
	@Query(value="from EventList e where e.restaurant = :restaurant")
	List<EventList> getByRest(Restaurant restaurant,Pageable page);
}
