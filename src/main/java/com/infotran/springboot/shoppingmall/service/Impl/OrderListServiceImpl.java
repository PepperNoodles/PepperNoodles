package com.infotran.springboot.shoppingmall.service.Impl;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.infotran.springboot.shoppingmall.dao.OrderListRepository;
import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.model.Product;
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
	public ArrayList<OrderList> findOrderList(Integer userid) {
		ArrayList<OrderList> order = ordao.findOrderList(userid);
		return order;
	}
	
	@Override
	public OrderList  findOrderListByUUID(long uuid){
		 return ordao.findOrderLisyByUUID(uuid);
	}	

	@Override
	public ArrayList<OrderDetail> findOrderDetailByFkOrderId(Integer orderid) {
		ArrayList<OrderDetail> orderdetailist =ordao.findOrderDetailByfkOrderId(orderid);
		return orderdetailist;
	}
	
	@Override
	public OrderList findById(Integer orderlistid) {
		Optional<OrderList> orlist = ordao.findById(orderlistid);
		OrderList order = null;
		if (orlist.isPresent()) {
			order = orlist.get();
		} else {
			throw new RuntimeException("Member(id=" + orderlistid + ")不存在");
		}
		return order;
	}
	
	
}
