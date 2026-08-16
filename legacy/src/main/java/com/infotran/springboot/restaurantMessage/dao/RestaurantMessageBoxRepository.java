package com.infotran.springboot.restaurantMessage.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.Restaurant;
import com.infotran.springboot.commonmodel.RestaurantMessageBox;
import com.infotran.springboot.commonmodel.UserAccount;

public interface RestaurantMessageBoxRepository extends JpaRepository<RestaurantMessageBox, Integer> {
	
	@Query(value="from RestaurantMessageBox r where r.restaurant = :restaurant")
	List<RestaurantMessageBox> getByRest(Restaurant restaurant,Pageable page);
	
	@Query(value="from RestaurantMessageBox r where r.restaurant = :restaurant and userAccount= :user")
	List<RestaurantMessageBox> getByRestAndUser(Restaurant restaurant,UserAccount user,Pageable page);
	
	@Query(value = "SELECT SUM(r.score) FROM RestaurantMessageBox r where r.restaurant = :restaurant")
	Integer restMessageRankSum(Restaurant restaurant);
	
	//@Query("SELECT COUNT(u) FROM User u WHERE u.name=?1")
	@Query(value = "SELECT COUNT(r) FROM RestaurantMessageBox r where r.restaurant = :restaurant and score != null")
	Integer restMessageNotNull(Restaurant restaurant);
	
}
