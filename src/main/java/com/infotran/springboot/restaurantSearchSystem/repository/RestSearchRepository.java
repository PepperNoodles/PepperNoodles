package com.infotran.springboot.restaurantSearchSystem.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.Restaurant;

public interface RestSearchRepository extends JpaRepository<Restaurant, Integer> {
	
	@Query(value="from Restaurant r Where r.restaurantName like concat('%',:restName,'%')",nativeQuery = false)
	public List<Restaurant> findRestaurantNameLike(String restName,Pageable pageable);
	
	@Query(value="from Restaurant r Where r.restaurantAddress like concat('%',:restAddress,'%')",nativeQuery = false)
	public List<Restaurant> findAddressNameLike(String restAddress,Pageable pageable);
	
	
	@Query(value="select r "
			+ "from Restaurant r "
			+ "join r.foodTag ftg "
			+ "Where r.restaurantName like concat('%',:restName,'%') and ftg.foodTagName = :tagName ")
	public List<Restaurant> findNameAndTag(String restName,String tagName,Pageable pageable);
	
	
	
	
	
}
