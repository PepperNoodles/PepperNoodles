package com.infotran.springboot.companysystem.service;

import java.util.List;

import com.infotran.springboot.commonmodel.EventList;
import com.infotran.springboot.commonmodel.Restaurant;

public interface EventListService {
	EventList insert(EventList event);
	EventList update(EventList event);
	void deleteById(Integer id);
	EventList findById(Integer id);
	
	List<EventList> getByRest(Restaurant rest);
}
