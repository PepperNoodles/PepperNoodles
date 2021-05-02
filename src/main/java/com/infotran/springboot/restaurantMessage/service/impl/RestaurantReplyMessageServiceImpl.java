package com.infotran.springboot.restaurantMessage.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.RestaurantReplyMessage;
import com.infotran.springboot.restaurantMessage.dao.RestaurantReplyMessageRepository;
import com.infotran.springboot.restaurantMessage.service.RestaurantReplyMessageService;

@Service
public class RestaurantReplyMessageServiceImpl implements RestaurantReplyMessageService {
	
	@Autowired
	RestaurantReplyMessageRepository restaurantReplyMessageRepository;

	@Override
	public RestaurantReplyMessage insert(RestaurantReplyMessage restaurantReplyMessage) {
		return restaurantReplyMessageRepository.save(restaurantReplyMessage);
	}

	@Override
	public RestaurantReplyMessage update(RestaurantReplyMessage restaurantReplyMessage) {
		return restaurantReplyMessageRepository.save(restaurantReplyMessage);
	}

	@Override
	public void delete(Integer id) {
		restaurantReplyMessageRepository.deleteById(id);
	}

	@Override
	public RestaurantReplyMessage findById(Integer id) {
		Optional<RestaurantReplyMessage> restaurantReplyMessageopt = restaurantReplyMessageRepository.findById(id);
		RestaurantReplyMessage restaurantReplyMessage=null;
		if (restaurantReplyMessageopt.isPresent()) {
			restaurantReplyMessage = restaurantReplyMessageopt.get();
		} else {
			throw new RuntimeException("restaurantReplyMessage(id=" + id + ")不存在");
		}
		return restaurantReplyMessage;
	}

	@Override
	public List<RestaurantReplyMessage> getByRestMessageBox(RestaurantMessageBox restMessageBox) {
		Pageable pageable = PageRequest.of(0, 200, Sort.Direction.ASC, "restaurantReplyId");
		List<RestaurantReplyMessage> restaurantReplyMessage = restaurantReplyMessageRepository.getByRestMessageBox(restMessageBox, pageable);
		return restaurantReplyMessage;
	}

}
