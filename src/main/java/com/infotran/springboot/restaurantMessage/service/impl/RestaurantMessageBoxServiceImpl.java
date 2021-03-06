package com.infotran.springboot.restaurantMessage.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.UserAccount;
import com.infotran.springboot.restaurantMessage.dao.RestaurantMessageBoxRepository;
import com.infotran.springboot.restaurantMessage.service.RestaurantMessageBoxService;

@Service
public class RestaurantMessageBoxServiceImpl implements RestaurantMessageBoxService {
	
	@Autowired
	RestaurantMessageBoxRepository restaurantMessageBoxDao;

	@Override
	public RestaurantMessageBox insert(RestaurantMessageBox restaurantMessageBox) {
		return restaurantMessageBoxDao.save(restaurantMessageBox);
	}

	@Override
	public RestaurantMessageBox update(RestaurantMessageBox restaurantMessageBox) {
		return restaurantMessageBoxDao.save(restaurantMessageBox);
	}

	@Override
	public void delete(Integer id) {
		restaurantMessageBoxDao.deleteById(id);
	}

	@Override
	public RestaurantMessageBox findById(Integer id) {
		Optional<RestaurantMessageBox> RestaurantMessageBoxOptional = restaurantMessageBoxDao.findById(id);
		RestaurantMessageBox restaurantMessageBox = null;
		if (RestaurantMessageBoxOptional.isPresent()) {
			restaurantMessageBox = RestaurantMessageBoxOptional.get();
		} else {
			throw new RuntimeException("restaurantMessageBox(id=" + id + ")不存在");
		}
		return restaurantMessageBox;
	}

	@Override
	public List<RestaurantMessageBox> getByRest(Restaurant rest) {
		Pageable pageable = PageRequest.of(0, 200, Sort.Direction.ASC, "restaurantMessageId");
		List<RestaurantMessageBox> restaurantMessageBox = restaurantMessageBoxDao.getByRest(rest, pageable);
		return restaurantMessageBox;
	}

	@Override
	public List<RestaurantMessageBox> getByRestAndUser(Restaurant restaurant, UserAccount user) {
		Pageable pageable = PageRequest.of(0, 200, Sort.Direction.ASC, "restaurantMessageId");
		List<RestaurantMessageBox> restaurantMessageBox = restaurantMessageBoxDao.getByRestAndUser(restaurant, user, pageable);
		return restaurantMessageBox;
	}

	@Override
	public String avgRank(Restaurant restaurant) {
		Integer sum = restaurantMessageBoxDao.restMessageRankSum(restaurant);
		Integer count = restaurantMessageBoxDao.restMessageNotNull(restaurant);
		if(sum != null && count != null) {
			Double sumdb = Double.valueOf(sum);	
			Double countdb = Double.valueOf(count);
			
			Double avgRank =(sumdb/countdb);
			String result = String.format("%2.1f" ,avgRank) ;		
			return result;
		}else {
			return null;
		}
		
	
	}

}
