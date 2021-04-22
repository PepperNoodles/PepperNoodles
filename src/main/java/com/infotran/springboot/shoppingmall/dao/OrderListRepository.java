package com.infotran.springboot.shoppingmall.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.infotran.springboot.shoppingmall.model.OrderList;

public interface OrderListRepository extends JpaRepository<OrderList, Integer> {

}
