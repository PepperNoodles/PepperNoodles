package com.infotran.springboot.companysystem.dao;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.commonmodel.MenuDetail;
import com.infotran.springboot.commonmodel.Restaurant;

public interface MenuDetailRepository extends JpaRepository<MenuDetail, Integer> {

	@Query(value="from MenuDetail m where m.restaurant = :restaurant")
	List<MenuDetail> getByRest(Restaurant restaurant,Pageable page);
}
