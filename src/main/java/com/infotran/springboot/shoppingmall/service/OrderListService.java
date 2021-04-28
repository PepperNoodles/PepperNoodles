package com.infotran.springboot.shoppingmall.service;

import java.util.ArrayList;

import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;

public interface OrderListService {
	
	
	public OrderList save (OrderList olist);

	ArrayList<OrderList> findOrderList(Integer userid);

	OrderList findOrderListByUUID(long uuid);

	ArrayList<OrderDetail> findOrderDetailByFkOrderId(Integer orderid);

	OrderList findById(Integer orderlistid);

	void delete(Integer orderlistid);

}
