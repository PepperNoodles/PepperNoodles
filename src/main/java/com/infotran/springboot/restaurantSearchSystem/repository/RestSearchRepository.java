package com.infotran.springboot.restaurantSearchSystem.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.Restaurant;

public interface RestSearchRepository extends JpaRepository<Restaurant, Integer> {
	
	
	public Optional<Restaurant> findByRestaurantName(String name);
	
	@Query(value="from Restaurant r Where r.restaurantName like concat('%',:restName,'%') or r.restaurantAddress like concat('%',:restName,'%')",nativeQuery = false)
	public Page<Restaurant> findRestaurantNameLike(String restName,Pageable pageable);
	
	@Query(value="from Restaurant r Where r.restaurantAddress like concat('%',:restAddress,'%')",nativeQuery = false)
	public List<Restaurant> findAddressNameLike(String restAddress,Pageable pageable);
	
	
	@Query(value="select r "
			+ "from Restaurant r "
			+ "join r.foodTag ftg "
			+ "Where r.restaurantName like concat('%',:restName,'%') and ftg.foodTagName = :tagName ")
	public List<Restaurant> findNameAndTag(String restName,String tagName,Pageable pageable);
	
	
	@Query(value="from Restaurant r "
			+ "Where r.latitude between :smallLat and :bigLat "
			+ "and r.longitude between :smallLong and :bigLong",nativeQuery = false)
	public List<Restaurant> findNearMap(BigDecimal smallLat ,BigDecimal bigLat,BigDecimal smallLong,BigDecimal bigLong);
	
	@Query(value="from Restaurant r "
			+ "Where r.latitude between :smallLat  and :bigLat",nativeQuery = false)
	public List<Restaurant> findNearMapLat(BigDecimal smallLat,BigDecimal bigLat);
	
	//tag + search
	@Query(value="select r "
			+ "from Restaurant r "
			+ "join r.foodTag ftg "
			+ "Where r.restaurantName like concat('%',:restName,'%') and ftg.foodTagName = :tagName "
			+ "or r.restaurantAddress like concat('%',:restName,'%') and ftg.foodTagName = :tagName")
	public List<Restaurant> findSearchNameAndTag(String restName,String tagName,Pageable pageable);
	
	//district + search
	@Query(value="from Restaurant r "
			+ " Where (r.restaurantName like concat('%',:restName,'%') or r.restaurantAddress like concat('%',:restName,'%')) "
			+ " and r.restaurantAddress like concat('%',:distName,'%') ")
	public List<Restaurant> findASearchtNameAndDistName(String restName,String distName,Pageable pageable);
	
	//district + tag + Name
	@Query(value="select r "
			+ "from Restaurant r "
			+ "join r.foodTag ftg "
			+ "Where (r.restaurantName like concat('%',:restName,'%') or r.restaurantAddress like concat('%',:restName,'%')) "
			+ "and ftg.foodTagName = :tagName "
			+ "and r.restaurantAddress like concat('%',:distName,'%')")
	public List<Restaurant> findSearchNameAndDistAndTag(String restName,String distName,String tagName,Pageable pageable);
	
}
