package com.infotran.springboot.restaurantSearchSystem.service.impl;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.restaurantSearchSystem.repository.RestSearchRepository;
import com.infotran.springboot.restaurantSearchSystem.service.RestSearchService;

@Service
public class RestSearchServiceImpl implements RestSearchService {

	@Autowired
	private RestSearchRepository restSearchRepository;	
	
	@Override
	public List<Restaurant> findRestaurantNameLike(String restName) {
		Pageable pageable = PageRequest.of(0, 6, Sort.Direction.DESC, "longitude");
		List<Restaurant> rests = restSearchRepository.findRestaurantNameLike(restName, pageable);
		return rests;
	}

	@Override
	public List<Restaurant> findAddressNameLike(String restAddr) {
		Pageable pageable = PageRequest.of(0, 6, Sort.Direction.DESC, "longitude");
		List<Restaurant> rests = restSearchRepository.findAddressNameLike(restAddr, pageable);
		return rests;
	}

	@Override
	public Restaurant getById(Integer id) {
		Optional<Restaurant> restOption = restSearchRepository.findById(id);
		if(restOption.isPresent()) {
			Restaurant rest = restOption.get();
			return rest;
		}else {
			return null;
		}		
	}

	@Override
	public List<Restaurant> findNameAndTag(String restName, String tagName) {
		Pageable pageable = PageRequest.of(0, 6, Sort.Direction.DESC, "longitude");
		List<Restaurant> rests = restSearchRepository.findNameAndTag(restName, tagName, pageable);
		return rests;
	}

	@Override
	public List<Restaurant> findAll() {
		List<Restaurant> rests = restSearchRepository.findAll();
		return rests;
	}

}
