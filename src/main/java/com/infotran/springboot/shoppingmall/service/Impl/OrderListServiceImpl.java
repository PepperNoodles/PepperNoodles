package com.infotran.springboot.shoppingmall.service.Impl;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infotran.springboot.shoppingmall.dao.OrderListRepository;
import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.service.OrderListService;

import antlr.collections.List;

@Service
public class OrderListServiceImpl implements OrderListService {
	
	@Autowired
	OrderListRepository ordao;

	@Override
	public OrderList save(OrderList olist) {
		return ordao.save(olist);
	}
	
	
	@Override
	public ArrayList<OrderList> findOrderList(Integer userid) {
//		ArrayList<OrderList> orlist= new ArrayList<OrderList>();
		ArrayList<OrderList> order = ordao.findOrderList(userid);
		return order;
	}
	

}
