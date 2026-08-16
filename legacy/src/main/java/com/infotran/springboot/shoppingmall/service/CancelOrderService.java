package com.infotran.springboot.shoppingmall.service;

import java.util.concurrent.DelayQueue;

import com.infotran.springboot.shoppingmall.model.OrderList;

public interface CancelOrderService {
	
	
	/**
     * 取消订单
     */
    void cancelOrder();


	DelayQueue<OrderList> getOrder(OrderList orderlist);

}
