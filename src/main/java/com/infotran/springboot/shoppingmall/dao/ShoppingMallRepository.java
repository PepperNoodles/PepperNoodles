package com.infotran.springboot.shoppingmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import com.infotran.springboot.shoppingmall.model.Order;

public interface ShoppingMallRepository extends JpaRepository<Order, Integer> {

	
	
}
