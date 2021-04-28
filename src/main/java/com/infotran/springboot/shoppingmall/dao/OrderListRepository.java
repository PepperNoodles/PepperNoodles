package com.infotran.springboot.shoppingmall.dao;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.infotran.springboot.shoppingmall.model.OrderDetail;
import com.infotran.springboot.shoppingmall.model.OrderList;

public interface OrderListRepository extends JpaRepository<OrderList, Integer> {
	
	
	
	@Query(value="select orlist from OrderList orlist where orlist.user.accountId =?1")
	public ArrayList<OrderList> findOrderList (Integer userid);
 
	
	@Query(value="select orlist from OrderList orlist where orlist.uuid=?1")
	public OrderList findOrderLisyByUUID(long uuid);
	
	@Query(value="select odetail from OrderDetail odetail where odetail.orderlist.orderId=?1")
	public ArrayList<OrderDetail> findOrderDetailByfkOrderId(Integer orderid);
	
	
	public Optional<OrderList> findById(Integer orderlistid);
	
	
	
}
