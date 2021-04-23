package com.infotran.springboot.shoppingmall.service.Impl;

import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.infotran.springboot.shoppingmall.dao.OrderListRepository;
import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.model.OrderListView;
import com.infotran.springboot.shoppingmall.service.OrderListService;

@Service
public class OrderListServiceImpl implements OrderListService {
	
	@Autowired
	OrderListRepository ordao;

	@Override
	public OrderList save(OrderList olist) {
		return ordao.save(olist);
	}
	
	
	@Override
	public ArrayList<OrderListView> findOrderListView(String username) {
		return ordao.findOrderList(username);
	}
	
	

}
