package com.infotran.springboot.companysystem.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.EventList;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.companysystem.dao.EventListRepository;
import com.infotran.springboot.companysystem.service.EventListService;

@Service
public class EventListServiceImpl implements EventListService {
	
	@Autowired
	EventListRepository eventListlDao;

	@Override
	public EventList insert(EventList event) {
		return eventListlDao.save(event);
	}

	@Override
	public EventList update(EventList event) {
		return eventListlDao.save(event);
	}

	@Override
	public void deleteById(Integer id) {
		eventListlDao.deleteById(id);
	}

	@Override
	public EventList findById(Integer id) {
		Optional<EventList> eventListOptional = eventListlDao.findById(id);
		EventList event = null;
		if (eventListOptional.isPresent()) {
			event = eventListOptional.get();
		} else {
			throw new RuntimeException("Event(id=" + id + ")不存在");
		}
		return event;
		
	}

	@Override
	public List<EventList> getByRest(Restaurant rest) {
		Pageable pageable = PageRequest.of(0, 200, Sort.Direction.ASC, "eventListId");
		List<EventList> eventList = eventListlDao.getByRest(rest, pageable);
		return eventList;
	}

}
