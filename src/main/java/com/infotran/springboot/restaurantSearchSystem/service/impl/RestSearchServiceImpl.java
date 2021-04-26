package com.infotran.springboot.restaurantSearchSystem.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "totalScore");
		
		Page<Restaurant> restspage = restSearchRepository.findRestaurantNameLike(restName, pageable);
//		Integer pages = restspage.getTotalPages();		
		List<Restaurant> rests = restspage.getContent();
//		Map<String,Object> restMap = new HashMap<>();
//		restMap.put("rests",rests);
//		restMap.put("page",pages);
		
		return rests;
	}

	@Override
	public List<Restaurant> findAddressNameLike(String restAddr) {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "totalScore");
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
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "totalScore");
		List<Restaurant> rests = restSearchRepository.findNameAndTag(restName, tagName, pageable);
		return rests;
	}

	@Override
	public List<Restaurant> findAll() {
		List<Restaurant> rests = restSearchRepository.findAll();
		return rests;
	}

	@Override
	public List<Restaurant> findNearMap(BigDecimal smallLat,BigDecimal bigLat,BigDecimal smallLong,BigDecimal bigLong) {
		List<Restaurant> rests = restSearchRepository.findNearMap(smallLat, bigLat, smallLong, bigLong);
		return rests;
	}

	@Override
	public List<Restaurant> findNearMapLat(BigDecimal smallLat, BigDecimal bigLat) {
		List<Restaurant> rests = restSearchRepository.findNearMapLat(smallLat, bigLat);
		return rests;
	}

	@Override
	public List<Restaurant> findSearchNameAndTag(String restName, String tagName) {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "totalScore");
		List<Restaurant> rests= restSearchRepository.findSearchNameAndTag(restName, tagName, pageable);
		return rests;
	}

	@Override
	public List<Restaurant> findASearchtNameAndDistName(String restName, String distName) {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "totalScore");
		List<Restaurant> rests = restSearchRepository.findASearchtNameAndDistName(restName, distName, pageable);
		return rests;
	}

	@Override
	public List<Restaurant> findSearchNameAndDistAndTag(String restName, String distName, String tagName) {
		Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, Sort.Direction.DESC, "totalScore");
		List<Restaurant> rests = restSearchRepository.findSearchNameAndDistAndTag(restName, distName, tagName, pageable);
		return rests;
	}
	

}
