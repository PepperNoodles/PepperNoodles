package com.infotran.springboot.restaurantMessage.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.RestaurantReplyMessage;

public interface RestaurantReplyMessageRepository extends JpaRepository<RestaurantReplyMessage, Integer> {
	
	@Query(value="from RestaurantReplyMessage r where r.restaurantMessageBox = :restaurantMessageBox")
	List<RestaurantReplyMessage> getByRestMessageBox(RestaurantMessageBox restaurantMessageBox,Pageable page);
}
