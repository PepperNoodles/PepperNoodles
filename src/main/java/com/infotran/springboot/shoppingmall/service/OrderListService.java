package com.infotran.springboot.shoppingmall.service;

import java.util.ArrayList;

import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.model.OrderListView;


public interface OrderListService {
	
	
	public OrderList save (OrderList olist);

	ArrayList<OrderListView> findOrderListView(String username);

}
