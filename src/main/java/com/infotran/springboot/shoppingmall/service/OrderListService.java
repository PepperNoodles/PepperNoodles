package com.infotran.springboot.shoppingmall.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;

public interface OrderListService {
	
	
	public OrderList save (OrderList olist);

	ArrayList<OrderList> findOrderList(Integer userid);

	OrderList findOrderListByUUID(long uuid);

	ArrayList<OrderDetail> findOrderDetailByFkOrderId(Integer orderid);

	OrderList findById(Integer orderlistid);

	void delete(Integer orderlistid);

	Integer countSumByProductId(Integer productid);

	ArrayList<OrderList> findOrderListByDateBetween(Date stratDate, Date endDate);

	Integer sumBySameDateWithConvertTo111(String date);

	ArrayList<OrderList> findOrderListByPast7or3Days(String startDay, String endDate);

	ArrayList<OrderList> findOrderListArrayBySameDate(String Date);

}
