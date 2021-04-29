package com.infotran.springboot.shoppingmall.service.Impl;

import java.util.Date;
import java.util.concurrent.DelayQueue;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.infotran.springboot.shoppingmall.model.OrderList;
import com.infotran.springboot.shoppingmall.service.CancelOrderService;
import com.infotran.springboot.shoppingmall.service.OrderListService;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;

@Service
public class CancelOrderServiceImpl implements CancelOrderService {

	private Integer isStarted = 1;
	
	/**
    * 延迟队列，用来存放订单对象
    */
   DelayQueue<OrderList> queue = new DelayQueue<>();
	
   @Autowired
   OrderListService orderlistservice;
   
   @Resource
   private ThreadPoolTaskExecutor executorService;
	
	@Override
	public void cancelOrder() {
        executorService.submit(()->{
            try {
                System.out.println("開啟自動取消訂單，當前時間：" + DateUtil.date());
                while (isStarted == 1) {
                    try {
                        OrderList order = queue.take();
                        String status = order.getStatus();
                        if ("已付款".equals(status) ) {
                        	return;
                        }else {
                        	OrderList orderlist = orderlistservice.findById(order.getOrderId());
                        	System.out.println("訂單：" + order.getOrderId() + "付款超时，自動取消，當前時間：" + DateUtil.date());
                        	orderlist.setStatus("超時取消訂單");
                        	orderlistservice.save(orderlist);
//                        	orderlistservice.delete(orderlist.getOrderId());
                        	queue.clear();
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

	}

	@Override
	public DelayQueue<OrderList> getOrder(OrderList orderlist) {
		 executorService.submit(()->{
	            try {
	                Date date = DateUtil.date();
	                orderlist.setOrderCreatedDate(date);
	                orderlist.setCancelTime(DateUtil.offset(date, DateField.MINUTE, 10));
	                queue.add(orderlist);
	                System.out.println("有進來延遲對列嗎");
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        });
	        return queue;
	    }

}
